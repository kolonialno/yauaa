package nl.basjes.parse.useragent.clienthints.parsers;

import nl.basjes.parse.useragent.clienthints.ClientHints;

import javax.annotation.Nonnull;
import java.util.Map;

public class ParseSecChUaWoW64 implements CHParser {
    public static final String HEADER_FIELD = "Sec-CH-UA-WoW64";

    //   From https://wicg.github.io/ua-client-hints/#http-ua-hints
    //
    //   3.10. The 'Sec-CH-UA-WoW64' Header Field
    //   The Sec-CH-UA-WoW64 request header field gives a server information about whether or not a user agent
    //   binary is running in 32-bit mode on 64-bit Windows.
    //   It is a Structured Header whose value MUST be a boolean [RFC8941].
    //
    //   The header’s ABNF is:
    //
    //   Sec-CH-UA-WoW64 = sf-boolean

    @Override
    public ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName) {
        String input = clientHintsHeaders.get(headerName);
        if (input == null) {
            return clientHints;
        }
        Boolean parsedBoolean = parseBoolean(input);
        if (parsedBoolean != null) {
            clientHints.setWow64(parsedBoolean);
        }
        return clientHints;
    }

    @Nonnull
    @Override
    public String inputField() {
        return HEADER_FIELD;
    }
}
