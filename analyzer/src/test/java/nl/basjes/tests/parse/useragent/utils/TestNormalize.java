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

package nl.basjes.tests.parse.useragent.utils;

import nl.basjes.parse.useragent.utils.Normalize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestNormalize {

    @Test
    void checkBrandEdgeCases() {
        assertNull(Normalize.brand(null));
        assertEquals("", Normalize.brand(""));
    }

    @Test
    void checkBrandOne() {
        assertEquals("N", Normalize.brand("n"));
        assertEquals("N", Normalize.brand("N"));
    }

    @Test
    void checkBrandTwo() {
        assertEquals("NB", Normalize.brand("nb"));
        assertEquals("NB", Normalize.brand("nB"));
        assertEquals("NB", Normalize.brand("Nb"));
        assertEquals("NB", Normalize.brand("NB"));
    }

    @Test
    void checkBrandThree() {
        assertEquals("NBA", Normalize.brand("nba"));
        assertEquals("NBA", Normalize.brand("nBa"));
        assertEquals("NBA", Normalize.brand("Nba"));
        assertEquals("NBA", Normalize.brand("NBA"));
    }

    @Test
    void checkBrandThreeFour() {
        assertEquals("NBA/Klmn", Normalize.brand("nba/kLmN"));
        assertEquals("NBA/Klmn", Normalize.brand("nBa/KlMn"));
        assertEquals("NBA/Klmn", Normalize.brand("Nba/klmn"));
        assertEquals("NBA/Klmn", Normalize.brand("NBA/KLMN"));
    }

    @Test
    void checkBrandNormalizationWord() {
        assertEquals("Niels", Normalize.brand("niels"));
        assertEquals("Niels", Normalize.brand("Niels"));
        assertEquals("Niels", Normalize.brand("NiElS"));
        assertEquals("Niels", Normalize.brand("nIELS"));
        assertEquals("Niels", Normalize.brand("NIELS"));
    }

    @Test
    void checkBrandNormalizationExamples() {
        // At least 3 lowercase
        assertEquals("NielsBasjes",      Normalize.brand("NielsBasjes"));
        assertEquals("NielsBasjes",      Normalize.brand("NIelsBasJES"));
        assertEquals("BlackBerry",       Normalize.brand("BlackBerry"));

        // Less than 3 lowercase
        assertEquals("Nielsbasjes",      Normalize.brand("NIelSBasJES"));
        assertEquals("Blackberry",       Normalize.brand("BLACKBERRY"));

        // Multiple words. Short words (1,2,3 letters) go full uppercase
        assertEquals("Niels NBA Basjes", Normalize.brand("NIels NbA BasJES"));
        assertEquals("LG",               Normalize.brand("lG"));
        assertEquals("HTC",              Normalize.brand("hTc"));
        assertEquals("Sony",             Normalize.brand("sOnY"));
        assertEquals("Asus",             Normalize.brand("aSuS"));
    }

    @Test
    void checkCombiningDeviceNameAndBrand() {
        assertEquals("Asus Something T123",     Normalize.cleanupDeviceBrandName("AsUs", "something t123"));
        assertEquals("Sony X1",                 Normalize.cleanupDeviceBrandName("Sony", "sony x1"));
        assertEquals("Sony X1",                 Normalize.cleanupDeviceBrandName("Sony", "sony-x1"));
        assertEquals("Sony X1",                 Normalize.cleanupDeviceBrandName("Sony", "sonyx1"));
        assertEquals("HP SlateBook 10 X2 PC",   Normalize.cleanupDeviceBrandName("hP", "SlateBook 10 X2 PC"));
        assertEquals("Samsung GT-1234",         Normalize.cleanupDeviceBrandName("Samsung", "GT - 1234"));
    }

    @Test
    void checkEmailNormalization() {
        assertEquals("support@zite.com",                   Normalize.email("support [at] zite [dot] com"));
        assertEquals("austin@affectv.co.uk",               Normalize.email("austin at affectv dot co dot uk"));
        assertEquals("epicurus@gmail.com",                 Normalize.email("epicurus at gmail dot com"));
        assertEquals("buibui.bot@moquadv.com",             Normalize.email("buibui[dot]bot[\\xc3\\xa07]moquadv[dot]com"));
        assertEquals("maxpoint.crawler@maxpoint.com",      Normalize.email("maxpoint.crawler at maxpoint dot com"));
        assertEquals("help@moz.com",                       Normalize.email("help@moz.com"));
        assertEquals("crawler@example.com",                Normalize.email("crawler at example dot com"));
        assertEquals("yelpbot@yelp.com",                   Normalize.email("yelpbot at yelp dot com"));
        assertEquals("support@zite.com",                   Normalize.email("support [at] zite [dot] com"));
        assertEquals("support@safedns.com",                Normalize.email("support [at] safedns [dot] com"));
        assertEquals("search_comments@sensis.com.au",      Normalize.email("search_comments\\at\\sensis\\dot\\com\\dot\\au"));
        assertEquals("mms-crawler-support@yahoo-inc.com",  Normalize.email("mms dash crawler dash support at yahoo dash inc dot com"));
    }

    @Test
    void checkBadInputData() {
        // This used to trigger an exception in the underlying RegEx.
        assertNotNull(Normalize.cleanupDeviceBrandName("${N", "${N.Foo"));
    }

    @Test
    void checkIsLowerCase() {
        assertTrue(Normalize.isLowerCase("basjes0123456789`~!@#$%^&*()_-+={}[];:'\",.<>/?"));

        assertTrue(Normalize.isLowerCase("niels"));
        assertTrue(Normalize.isLowerCase("basjes"));

        assertFalse(Normalize.isLowerCase("Basjes"));
        assertFalse(Normalize.isLowerCase("bAsjes"));
        assertFalse(Normalize.isLowerCase("baSjes"));
        assertFalse(Normalize.isLowerCase("basJes"));
        assertFalse(Normalize.isLowerCase("basjEs"));
        assertFalse(Normalize.isLowerCase("basjeS"));
        assertFalse(Normalize.isLowerCase("BASJES"));
    }
}
