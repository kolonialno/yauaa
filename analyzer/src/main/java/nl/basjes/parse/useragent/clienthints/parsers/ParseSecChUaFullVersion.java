//package nl.basjes.parse.useragent.clienthints.parsers;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import nl.basjes.parse.useragent.clienthints.ClientHints;
//
//import javax.annotation.Nonnull;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentMap;
//
//public class ParseSecChUaFullVersion implements CHParser {
//
//    public static final String HEADER_FIELD = "Sec-CH-UA-Full-Version";
//
//    private transient ConcurrentMap<String, List<ClientHints.NavigatorUABrandVersion>> cache;
//
//    public ParseSecChUaFullVersion() {
//        cache = Caffeine.newBuilder().maximumSize(10000).<String, List<ClientHints.NavigatorUABrandVersion>>build().asMap();
//    }
//
//    //   From https://wicg.github.io/ua-client-hints/#http-ua-hints
//    //
//    //   3.4. The 'Sec-CH-UA-Full-Version' Header Field
//    //   Sec-CH-UA-Full-Version is deprecated and will be removed in the future.
//    //   Developers should use Sec-CH-UA-Full-Version-List instead.
//    //
//    //   The Sec-CH-UA-Full-Version request header field gives a server information about the user agent’s full version.
//    //   It is a Structured Header whose value MUST be a string [RFC8941].
//    //
//    //   The header’s ABNF is:
//    //
//    //     Sec-CH-UA-Full-Version = sf-string
//    //
//
//    @Override
//    public ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName) {
//        String input = clientHintsHeaders.get(headerName);
//        if (input == null) {
//            return clientHints;
//        }
//        List<ClientHints.NavigatorUABrandVersion> brandVersions = cache.computeIfAbsent(input, value -> BrandVersionListParser.parse(input));
//        clientHints.setBrands(brandVersions);
//        return clientHints;
//    }
//
//    @Nonnull
//    @Override
//    public String inputField() {
//        return HEADER_FIELD;
//    }
//}
