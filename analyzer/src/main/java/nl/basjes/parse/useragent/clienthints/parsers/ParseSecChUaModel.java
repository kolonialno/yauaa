package nl.basjes.parse.useragent.clienthints.parsers;

import nl.basjes.parse.useragent.clienthints.ClientHints;

import javax.annotation.Nonnull;
import java.util.Map;

public class ParseSecChUaModel implements CHParser {

    public static final String HEADER_FIELD = "Sec-CH-UA-Model";

    //   From https://wicg.github.io/ua-client-hints/#http-ua-hints
    //
    //   3.7. The 'Sec-CH-UA-Model' Header Field
    //   The Sec-CH-UA-Model request header field gives a server information about the device on which a given
    //   user agent is executing. It is a Structured Header whose value MUST be a string [RFC8941].
    //
    //   The header’s ABNF is:
    //
    //   Sec-CH-UA-Model = sf-string

    @Override
    public ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName) {
        String input = clientHintsHeaders.get(headerName);
        String value = parseSfString(input);
        if (value != null && !value.isEmpty()) {
            clientHints.setModel(value);
        }
        return clientHints;
    }

    @Nonnull
    @Override
    public String inputField() {
        return HEADER_FIELD;
    }
}
