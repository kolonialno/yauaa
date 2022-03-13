package nl.basjes.parse.useragent.clienthints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

// In call cases: null means not specified
public class ClientHints implements Serializable {
    // See: https://wicg.github.io/ua-client-hints/#interface
    @Getter @Setter private List<BrandVersion> brands = null;
    @Getter @Setter private Boolean mobile = null;
    @Getter @Setter private String architecture = null;
    @Getter @Setter private String bitness = null;
    @Getter @Setter private String model = null;
    @Getter @Setter private String platform = null;
    @Getter @Setter private String platformVersion = null;
    @Getter @Setter private Boolean wow64 = null;
    @Getter @Setter private List<BrandVersion> fullVersionList = null;

    @AllArgsConstructor
    public static class BrandVersion {
        @Getter @Setter private String brand;
        @Getter @Setter private String version;
    }

}

