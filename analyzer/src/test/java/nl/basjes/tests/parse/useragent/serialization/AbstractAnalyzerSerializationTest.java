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

package nl.basjes.tests.parse.useragent.serialization;

import nl.basjes.parse.useragent.AbstractUserAgentAnalyzerDirect;
import nl.basjes.parse.useragent.AbstractUserAgentAnalyzerDirect.AbstractUserAgentAnalyzerDirectBuilder;
import nl.basjes.parse.useragent.debug.UserAgentAnalyzerTester;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractAnalyzerSerializationTest<ANALYZER extends AbstractUserAgentAnalyzerDirect> {

    private static final Logger LOG = LogManager.getLogger(AbstractAnalyzerSerializationTest.class);

    abstract byte[] serialize(ANALYZER uaa) throws IOException;

    abstract ANALYZER deserialize(byte[] bytes) throws IOException, ClassNotFoundException;

    abstract ANALYZER create();

    public void configureTestInstance(AbstractUserAgentAnalyzerDirectBuilder<ANALYZER, ?> builder) {
        builder
            .dropDefaultResources()
            .addResources("classpath*:AllSteps.yaml")
            .addResources("classpath*:AllFields-tests.yaml")
            .addResources("classpath*:AllPossibleSteps.yaml")
            .addResources("classpath*:IsNullLookup.yaml")
            .keepTests();
    }

    @Test
    void serializeAndDeserializeTestsBefore() throws IOException, ClassNotFoundException {
        serializeAndDeserializeUAANormalInstances(create(), true);
    }

    @Test
    void serializeAndDeserialize() throws IOException, ClassNotFoundException {
        serializeAndDeserializeUAANormalInstances(create(), false);
    }

    private void serializeAndDeserializeUAANormalInstances(ANALYZER uaaBefore, boolean runTestsBefore) throws IOException, ClassNotFoundException {
        LOG.info("==============================================================");
        LOG.info("Create");
        LOG.info("--------------------------------------------------------------");

        if (runTestsBefore) {
            LOG.info("--------------------------------------------------------------");

            assertTrue(UserAgentAnalyzerTester.runTests(uaaBefore, false, false, null, false, false, null), "Tests BEFORE serialization failed");

            // Get rid of the data of the last tested useragent
            uaaBefore.reset();
        }

        LOG.info("--------------------------------------------------------------");
        LOG.info("Serialize");

        long   serializeStartNs = System.nanoTime();
        byte[] bytes            = serialize(uaaBefore);
        long   serializeStopNs  = System.nanoTime();

        LOG.info("Serialize took {} ns ({} ms)", serializeStopNs - serializeStartNs, (serializeStopNs - serializeStartNs) / 1_000_000);
        LOG.info("The UserAgentAnalyzer was serialized into {} bytes", bytes.length);
        LOG.info("--------------------------------------------------------------");
        LOG.info("Deserialize");

        long deserializeStartNs = System.nanoTime();
        AbstractUserAgentAnalyzerDirect uaaAfter = deserialize(bytes);
        long deserializeStopNs = System.nanoTime();

        LOG.info("Done");
        LOG.info("Deserialize took {} ns ({} ms)", deserializeStopNs - deserializeStartNs, (deserializeStopNs - deserializeStartNs) / 1_000_000);

        LOG.info("==============================================================");
        LOG.info("Validating when getting all fields");
        LOG.info("--------------------------------------------------------------");
        assertTrue(UserAgentAnalyzerTester.runTests(uaaAfter, false, false, null, false, false, null), "Tests AFTER serialization failed");
        LOG.info("==============================================================");
    }



}
