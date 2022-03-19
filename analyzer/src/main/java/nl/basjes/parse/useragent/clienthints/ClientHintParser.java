/*
 * Yet Another UserAgent Analyzer
 * Copyright (C) 2013-2022 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.basjes.parse.useragent.clienthints;

import com.github.benmanes.caffeine.cache.Caffeine;
import nl.basjes.parse.useragent.clienthints.parsers.CHParser;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUa;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaArch;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaFullVersionList;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaMobile;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaModel;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaPlatform;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaPlatformVersion;
import nl.basjes.parse.useragent.clienthints.parsers.ParseSecChUaWoW64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ClientHintParser implements Serializable {
    public static final Logger LOG = LogManager.getFormatterLogger("CHParser");

    private Map<String, CHParser> parsers;
    public ClientHintParser() {
        parsers = new TreeMap<>();
        addParser(new ParseSecChUa()); // Ordering matters: this and the "Full version list" write to the same fields.
        addParser(new ParseSecChUaArch());
//        addParser(new ParseSecChUaFullVersion()); // Deprecated header
        addParser(new ParseSecChUaFullVersionList());
        addParser(new ParseSecChUaMobile());
        addParser(new ParseSecChUaModel());
        addParser(new ParseSecChUaPlatform());
        addParser(new ParseSecChUaPlatformVersion());
        addParser(new ParseSecChUaWoW64());
    }

    public List<String> supportedClientHintHeaders() {
        return parsers.values().stream().map(CHParser::inputField).distinct().collect(Collectors.toList());
    }

    private void addParser(CHParser parser) {
        String field = parser.inputField().toLowerCase(Locale.ROOT);
        if (parsers.containsKey(field)) {
            throw new IllegalStateException("We have two parsers for the same field (" + field + "): " +
                parsers.get(field).getClass().getSimpleName() +
                " and " +
                parser.getClass().getSimpleName());
        }
        parsers.put(field, parser);
    }

    public ClientHints parse(Map<String, String> requestHeaders) {
        ClientHints clientHints = new ClientHints();

        for (Map.Entry<String, String> headerEntry : requestHeaders.entrySet()) {
            String headerName = headerEntry.getKey();
            CHParser parser = parsers.get(headerName.toLowerCase(Locale.ROOT));
            if (parser != null) {
                parser.parse(requestHeaders, clientHints, headerName);
            }
        }
        return clientHints;
    }

    @Override
    public String toString() {
        return "ClientHintAnalyzer:" + getClass().getSimpleName();
    }

    public interface ClientHintCacheInstantiator<T extends Serializable> extends Serializable {
        /**
         * A single method that must create a new instance of the cache.
         * The returned instance MUST implement at least the {@link Map#get} and {@link Map#put}
         * methods in a threadsafe way if you intend to use this in a multithreaded scenario.
         * Yauaa only uses the put and get methods and in exceptional cases the clear method.
         * An implementation that does some kind of automatic cleaning of obsolete values is recommended (like LRU).
         * @param cacheSize is the size of the new cache (which will be >= 1)
         * @return Instance of the new cache.
         */
        ConcurrentMap<String, T> instantiateCache(int cacheSize);
    }

    static class DefaultClientHintCacheInstantiator<T extends Serializable> implements ClientHintCacheInstantiator<T> {
        public ConcurrentMap<String, T> instantiateCache(int cacheSize) {
            return Caffeine.newBuilder().maximumSize(cacheSize).<String, T>build().asMap();
        }
    }

    private ClientHintCacheInstantiator<?> clientHintCacheInstantiator = new DefaultClientHintCacheInstantiator<>();
    private int cacheSize;

    public void setHintCacheInstantiator(ClientHintCacheInstantiator<?> newClientHintCacheInstantiator) {
        this.clientHintCacheInstantiator = newClientHintCacheInstantiator;
    }

    public synchronized void initializeCache() {
        parsers.values().forEach(parser -> parser.initializeCache(clientHintCacheInstantiator, cacheSize));
    }

    public synchronized void clearCache() {
        parsers.values().forEach(CHParser::clearCache);
    }
}
