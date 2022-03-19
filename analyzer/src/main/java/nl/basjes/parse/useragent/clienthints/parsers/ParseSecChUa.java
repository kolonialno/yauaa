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

package nl.basjes.parse.useragent.clienthints.parsers;

import nl.basjes.parse.useragent.clienthints.ClientHintParser.ClientHintCacheInstantiator;
import nl.basjes.parse.useragent.clienthints.ClientHints;
import nl.basjes.parse.useragent.clienthints.ClientHints.BrandVersion;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ParseSecChUa implements CHParser {

    public static final String HEADER_FIELD = "Sec-CH-UA";

    private transient ConcurrentMap<String, List<BrandVersion>> cache = null;

    public ParseSecChUa() {
        // Nothing to do right now
    }

    @SuppressWarnings("unchecked")
    public void initializeCache(ClientHintCacheInstantiator<?> clientHintCacheInstantiator, int cacheSize) {
        cache = (ConcurrentMap<String, List<BrandVersion>>) clientHintCacheInstantiator.instantiateCache(cacheSize);
    }

    public void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }

    //   From https://wicg.github.io/ua-client-hints/#http-ua-hints
    //
    //   3.1. The 'Sec-CH-UA' Header Field
    //   The Sec-CH-UA request header field gives a server information about a user agent's branding and version.
    //   It is a Structured Header whose value MUST be a list [RFC8941].
    //   The list’s items MUST be string.
    //   The value of each item SHOULD include a "v" parameter, indicating the user agent's version.
    //   The header’s ABNF is:
    //   Sec-CH-UA = sf-list
    //   To return the Sec-CH-UA value for a request, perform the following steps:
    //   Let brands be the result of running create brands with "significant version".
    //   Let list be the result of creating a brand-version list, with brands and "significant version".
    //
    //   Return the output of running serializing a list with list.
    //   Note: Unlike most Client Hints, since it’s included in the low entropy hint table,
    //          the Sec-CH-UA header will be sent by default, whether or not the server opted-into receiving
    //          the header via an Accept-CH header (although it can still be controlled by it’s policy controlled
    //          client hints feature. It is considered low entropy because it includes only the user agent's
    //          branding information, and the significant version number (both of which are fairly clearly sniffable
    //          by "examining the structure of other headers and by testing for the availability and semantics of
    //          the features introduced or modified between releases of a particular browser" [Janc2014]).

    @Override
    public ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName) {
        String input = clientHintsHeaders.get(headerName);
        if (input == null) {
            return clientHints;
        }
        // " Not A;Brand";v="99", "Chromium";v="99", "Google Chrome";v="99"
        List<BrandVersion> brandVersions = cache.computeIfAbsent(input, value -> BrandVersionListParser.parse(input));
        if (!brandVersions.isEmpty()) {
            clientHints.setBrands(brandVersions);
        }

        return clientHints;
    }

    @Nonnull
    @Override
    public String inputField() {
        return HEADER_FIELD;
    }
}
