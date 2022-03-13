package nl.basjes.parse.useragent.clienthints.parsers;

import nl.basjes.parse.useragent.clienthints.ClientHints;

import javax.annotation.Nonnull;
import java.util.Map;

public class ParseSecChUaBitness implements CHParser {
    public static final String HEADER_FIELD = "Sec-CH-UA-Bitness";

    //   From https://wicg.github.io/ua-client-hints/#http-ua-hints
    //
    //   3.3. The 'Sec-CH-UA-Bitness' Header Field
    //   The Sec-CH-UA-Bitness request header field gives a server information about the bitness of the architecture
    //   of the platform on which a given user agent is executing.
    //   It is a Structured Header whose value MUST be a string [RFC8941].
    //   The header’s ABNF is:
    //      Sec-CH-UA-Bitness = sf-string

    @Override
    public ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName) {
        String input = clientHintsHeaders.get(headerName);
        String value = parseSfString(input);
        if (value != null && !value.isEmpty()) {
            clientHints.setBitness(input);
        }
        return clientHints;
    }

    @Nonnull
    @Override
    public String inputField() {
        return HEADER_FIELD;
    }
}
