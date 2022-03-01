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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TestLoggingColors {
    private static final Logger LOG = LogManager.getLogger(TestLoggingColors.class);

    @Test
    void testLoggingColors() {
        assertDoesNotThrow(() -> {
            LOG.trace("Message to check the colors of \"Trace\"");
            LOG.debug("Message to check the colors of \"Debug\"");
            LOG.info("Message to check the colors of \"Info\"");
            LOG.warn("Message to check the colors of \"Warn\"");
            LOG.error("Message to check the colors of \"Error\"");
            LOG.fatal("Message to check the colors of \"Fatal\"");
        });

    }
}
