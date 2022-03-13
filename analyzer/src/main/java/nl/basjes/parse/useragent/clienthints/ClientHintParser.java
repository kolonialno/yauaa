package nl.basjes.parse.useragent.clienthints;

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

}
