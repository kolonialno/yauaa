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

package nl.basjes.parse.useragent.clienthints;

import nl.basjes.parse.useragent.UserAgent.ImmutableUserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
class TestClientHintsAnalysis {

    private static final Logger LOG = LogManager.getFormatterLogger(TestClientHintsAnalysis.class);

    // ------------------------------------------

    static UserAgentAnalyzer analyzer;

    @BeforeAll
    static void beforeAll() {
        analyzer = UserAgentAnalyzer.newBuilder().build();
    }

    private void checkExpectations(ImmutableUserAgent userAgent, Map<String, String> expectations){
        for (Map.Entry<String, String> expectation : expectations.entrySet()) {
            assertEquals(expectation.getValue(), userAgent.getValue(expectation.getKey()));
        }
    }


    @Test
    void testChromeWindows11() {
        ImmutableUserAgent userAgent;
        Map<String, String> expectations = new TreeMap<>();
        // Real values Chrome on Windows 11
        Map<String, String> headers=new TreeMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36");

        // ------------------------------------------
        // Without ClientHints
        expectations.put("DeviceClass",                     "Desktop");
        expectations.put("DeviceName",                      "Desktop");
        expectations.put("DeviceBrand",                     "Unknown");
        expectations.put("DeviceCpu",                       "Intel x86_64");
        expectations.put("DeviceCpuBits",                   "64");
        expectations.put("OperatingSystemClass",            "Desktop");
        expectations.put("OperatingSystemName",             "Windows NT");
        expectations.put("OperatingSystemVersion",          ">=10");
        expectations.put("OperatingSystemVersionMajor",     ">=10");
        expectations.put("OperatingSystemNameVersion",      "Windows >=10");
        expectations.put("OperatingSystemNameVersionMajor", "Windows >=10");
        expectations.put("LayoutEngineClass",               "Browser");
        expectations.put("LayoutEngineName",                "Blink");
        expectations.put("LayoutEngineVersion",             "99.0");
        expectations.put("LayoutEngineVersionMajor",        "99");
        expectations.put("LayoutEngineNameVersion",         "Blink 99.0");
        expectations.put("LayoutEngineNameVersionMajor",    "Blink 99");
        expectations.put("AgentClass",                      "Browser");
        expectations.put("AgentName",                       "Chrome");
        expectations.put("AgentVersion",                    "99.0.4844.51");
        expectations.put("AgentVersionMajor",               "99");
        expectations.put("AgentNameVersion",                "Chrome 99.0.4844.51");
        expectations.put("AgentNameVersionMajor",           "Chrome 99");

        userAgent = analyzer.parse(headers);
        checkExpectations(userAgent, expectations);

        // ------------------------------------------
        // Add Standard ClientHints
        headers.put("Sec-CH-UA",                            "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        headers.put("Sec-CH-UA-Mobile",                     "?0");
        headers.put("Sec-CH-UA-Platform",                   "\"Windows\"");
        // No change in expectations.
        userAgent = analyzer.parse(headers);
        checkExpectations(userAgent, expectations);

        // ------------------------------------------
        // Add the extra ClientHints (i.e. after the server requested everything)
        headers.put("Sec-CH-UA-Arch",                       "\"x86\"");
        headers.put("Sec-CH-UA-Full-Version-List",          "\" Not A;Brand\";v=\"99.0.0.0\", \"Chromium\";v=\"99.0.4844.51\", \"Google Chrome\";v=\"99.0.4844.51\"");
        headers.put("Sec-CH-UA-Model",                      "");
        headers.put("Sec-CH-UA-Platform-Version",           "\"14.0.0\"");

        expectations.put("OperatingSystemVersion",          "11");
        expectations.put("OperatingSystemVersionMajor",     "11");
        expectations.put("OperatingSystemNameVersion",      "Windows 11");
        expectations.put("OperatingSystemNameVersionMajor", "Windows 11");

        expectations.put("LayoutEngineVersion",             "99.0.4844.51");
        expectations.put("LayoutEngineNameVersion",         "Blink 99.0.4844.51");

        expectations.put("AgentVersion",                    "99.0.4844.51");
        expectations.put("AgentNameVersion",                "Chrome 99.0.4844.51");

        userAgent = analyzer.parse(headers);
        checkExpectations(userAgent, expectations);
    }

    @Test
    void testChromeWindows11ReducedUA() {
        Map<String, String> expectations = new TreeMap<>();

        // Real values Chrome on Windows 11
        Map<String, String> headers=new TreeMap<>();
        headers.put("User-Agent",  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.0.0 Safari/537.36");

        // ------------------------------------------
        // Without ClientHints
        expectations.put("DeviceClass",                     "Desktop");
        expectations.put("DeviceName",                      "Desktop");
        expectations.put("DeviceBrand",                     "Unknown");
        expectations.put("DeviceCpu",                       "Intel x86_64");
        expectations.put("DeviceCpuBits",                   "64");
        expectations.put("OperatingSystemClass",            "Desktop");
        expectations.put("OperatingSystemName",             "Windows NT");
        expectations.put("OperatingSystemVersion",          "??");
        expectations.put("OperatingSystemVersionMajor",     "??");
        expectations.put("OperatingSystemNameVersion",      "Windows NT ??");
        expectations.put("OperatingSystemNameVersionMajor", "Windows NT ??");
        expectations.put("LayoutEngineClass",               "Browser");
        expectations.put("LayoutEngineName",                "Blink");
        expectations.put("LayoutEngineVersion",             "99");
        expectations.put("LayoutEngineVersionMajor",        "99");
        expectations.put("LayoutEngineNameVersion",         "Blink 99");
        expectations.put("LayoutEngineNameVersionMajor",    "Blink 99");
        expectations.put("AgentClass",                      "Browser");
        expectations.put("AgentName",                       "Chrome");
        expectations.put("AgentVersion",                    "99");
        expectations.put("AgentVersionMajor",               "99");
        expectations.put("AgentNameVersion",                "Chrome 99");
        expectations.put("AgentNameVersionMajor",           "Chrome 99");
        checkExpectations(analyzer.parse(headers), expectations);

        // ------------------------------------------
        // Add Standard ClientHints
        headers.put("Sec-CH-UA",                            "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        headers.put("Sec-CH-UA-Mobile",                     "?0");
        headers.put("Sec-CH-UA-Platform",                   "\"Windows\"");
        // No change in expectations.
        checkExpectations(analyzer.parse(headers), expectations);

        // ------------------------------------------
        // Add Full ClientHints (i.e. after the server requested everything)
        headers.put("Sec-CH-UA-Arch",                       "\"x86\"");
        headers.put("Sec-CH-UA-Full-Version-List",          "\" Not A;Brand\";v=\"99.0.0.0\", \"Chromium\";v=\"99.0.4844.51\", \"Google Chrome\";v=\"99.0.4844.51\"");
        headers.put("Sec-CH-UA-Model",                      "\"\"");
        headers.put("Sec-CH-UA-Platform-Version",           "\"14.0.0\"");

        expectations.put("OperatingSystemVersion",          "11");
        expectations.put("OperatingSystemVersionMajor",     "11");
        expectations.put("OperatingSystemNameVersion",      "Windows 11");
        expectations.put("OperatingSystemNameVersionMajor", "Windows 11");

        expectations.put("LayoutEngineVersion",             "99.0.4844.51");
        expectations.put("LayoutEngineNameVersion",         "Blink 99.0.4844.51");

        expectations.put("AgentVersion",                    "99.0.4844.51");
        expectations.put("AgentNameVersion",                "Chrome 99.0.4844.51");

        checkExpectations(analyzer.parse(headers), expectations);
    }

    @Test
    void testChromeAndroid11ReducedUA() {
        Map<String, String> expectations = new TreeMap<>();

        // Real values Chrome on Windows 11
        Map<String, String> headers=new TreeMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Mobile Safari/537.36");

        // ------------------------------------------
        // Without ClientHints
        expectations.put("DeviceClass",                     "Phone");
        expectations.put("DeviceName",                      "Unknown");
        expectations.put("DeviceBrand",                     "Unknown");
        expectations.put("OperatingSystemClass",            "Mobile");
        expectations.put("OperatingSystemName",             "Android");
        expectations.put("OperatingSystemVersion",          "??");
        expectations.put("OperatingSystemVersionMajor",     "??");
        expectations.put("OperatingSystemNameVersion",      "Android ??");
        expectations.put("OperatingSystemNameVersionMajor", "Android ??");
        expectations.put("LayoutEngineClass",               "Browser");
        expectations.put("LayoutEngineName",                "Blink");
        expectations.put("LayoutEngineVersion",             "100");
        expectations.put("LayoutEngineVersionMajor",        "100");
        expectations.put("LayoutEngineNameVersion",         "Blink 100");
        expectations.put("LayoutEngineNameVersionMajor",    "Blink 100");
        expectations.put("AgentClass",                      "Browser");
        expectations.put("AgentName",                       "Chrome");
        expectations.put("AgentVersion",                    "100");
        expectations.put("AgentVersionMajor",               "100");
        expectations.put("AgentNameVersion",                "Chrome 100");
        expectations.put("AgentNameVersionMajor",           "Chrome 100");


        checkExpectations(analyzer.parse(headers), expectations);

        // ------------------------------------------
        // Add Standard ClientHints
        headers.put("Sec-CH-UA",                            "\"(Not(A:Brand\";v=\"8\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"");
        headers.put("Sec-CH-UA-Mobile",                     "?1");
        headers.put("Sec-CH-UA-Platform",                   "\"Android\"");

        // No change in expectations.
        checkExpectations(analyzer.parse(headers), expectations);

        // ------------------------------------------
        // Add Full ClientHints (i.e. after the server requested everything)
        headers.put("Sec-CH-UA-Arch",                       "");
        headers.put("Sec-CH-UA-Full-Version-List",          "\"(Not(A:Brand\";v=\"8.0.0.0\", \"Chromium\";v=\"100.0.4896.30\", \"Google Chrome\";v=\"100.0.4896.30\"");
        headers.put("Sec-CH-UA-Model",                      "\"Nokia 7.2\"");
        headers.put("Sec-CH-UA-Platform-Version",           "\"11.0.0\"");

        expectations.put("DeviceClass",                     "Phone");
        expectations.put("DeviceBrand",                     "Nokia");
        expectations.put("DeviceName",                      "Nokia 7.2");

        expectations.put("OperatingSystemVersion",          "11.0.0");
        expectations.put("OperatingSystemVersionMajor",     "11");
        expectations.put("OperatingSystemNameVersion",      "Android 11.0.0");
        expectations.put("OperatingSystemNameVersionMajor", "Android 11");

        expectations.put("LayoutEngineVersion",             "100.0.4896.30");
        expectations.put("LayoutEngineNameVersion",         "Blink 100.0.4896.30");

        expectations.put("AgentVersion",                    "100.0.4896.30");
        expectations.put("AgentNameVersion",                "Chrome 100.0.4896.30");

        checkExpectations(analyzer.parse(headers), expectations);
    }

}
