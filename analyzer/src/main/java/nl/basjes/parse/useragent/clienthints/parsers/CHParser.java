package nl.basjes.parse.useragent.clienthints.parsers;

import nl.basjes.parse.useragent.clienthints.ClientHints;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;

public interface CHParser extends Serializable {
    /**
     * Parses the provided Client Hints request Headers and updates the provided ClientHints instance
     *
     * @param clientHintsHeaders The map of request headers that at least contains one of the supported fields
     * @param clientHints        The instance that is to be updated
     * @param headerName         The actual name of the header (may be different case than the expected inputfield)
     * @return The same instance as the provided clientHints parameter.
     */
    ClientHints parse(Map<String, String> clientHintsHeaders, ClientHints clientHints, String headerName);

    /**
     * What are the Client Hints that this parser can do something with?
     *
     * @return A list (non-empty) of request header names this parser can handle.
     */
    @Nonnull
    String inputField();

    /**
     * Determines if the provided value is a sf-boolean.
     * @param value The value to be parsed
     * @return True/False or null if this was NOT a boolean.
     */
    default Boolean parseBoolean(String value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case "?0" : return Boolean.FALSE;
            case "?1" : return Boolean.TRUE;
            default   : return null;
        }
    }

    /**
     * A sf-string is a String with '"' around it.
     * @param value The value to be parsed
     * @return The actual payload string (i.e. without the surrounding '"')
     */
    default String parseSfString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        int length = value.length();
        boolean startQuote = value.charAt(0) == '"';
        boolean endQuote = value.charAt(length-1) == '"';
        if (startQuote && endQuote) {
            return value.substring(1, length - 1).trim();
        }
        return null; // Bad input
    }
}
