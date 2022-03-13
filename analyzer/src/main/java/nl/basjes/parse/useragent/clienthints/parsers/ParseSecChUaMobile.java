package nl.basjes.parse.useragent.clienthints.parsers;

import nl.basjes.parse.useragent.clienthints.ClientHints;

import javax.annotation.Nonnull;
import java.util.Map;

public class ParseSecChUaMobile implements CHParser {
    public static final String HEADER_FIELD = "Sec-CH-UA-Mobile";

    //   From https://wicg.github.io/ua-client-hints/#http-ua-hints
    //
    //   3.6. The 'Sec-CH-UA-Mobile' Header Field
    //   The Sec-CH-UA-Mobile request header field gives a server information about whether or not a user agent
    //   prefers a "mobile" user experience. It is a Structured Header whose value MUST be a boolean [RFC8941].
    //
    //   The header’s ABNF is:
    //   Sec-CH-UA-Mobile = sf-boolean
    //   Note: Like Sec-CH-UA above, since it’s included in the low entropy hint table,
    //   the Sec-CH-UA-Mobile header will be sent by default,
    //   whether or not the server opted-into receiving the header via an Accept-CH header
    //   (although it can still be controlled by its policy controlled client hints feature).
    //   It is considered low entropy because it is a single bit of information directly controllable by the user.

    @Override
    public ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName) {
        String input = clientHintsHeaders.get(headerName);
        Boolean parsedBoolean = parseBoolean(input);
        if (parsedBoolean != null) {
            clientHints.setMobile(parsedBoolean);
        }
        return clientHints;
    }

    @Nonnull
    @Override
    public String inputField() {
        return HEADER_FIELD;
    }

}
