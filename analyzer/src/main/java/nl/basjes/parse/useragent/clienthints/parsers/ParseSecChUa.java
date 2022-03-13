package nl.basjes.parse.useragent.clienthints.parsers;

import com.github.benmanes.caffeine.cache.Caffeine;
import nl.basjes.parse.useragent.clienthints.ClientHints;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ParseSecChUa implements CHParser {

    public static final String HEADER_FIELD = "Sec-CH-UA";

    private transient ConcurrentMap<String, List<ClientHints.BrandVersion>> cache;

    public ParseSecChUa() {
        cache = Caffeine.newBuilder().maximumSize(10000).<String, List<ClientHints.BrandVersion>>build().asMap();
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
        List<ClientHints.BrandVersion> brandVersions = cache.computeIfAbsent(input, value -> BrandVersionListParser.parse(input));
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
