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

@SuppressWarnings({ "exports", "requires-automatic", "requires-transitive-automatic" })
module nl.basjes.parse.useragent {
    // Allow loading the rules via reflection and export all packages.
    opens UserAgents;
    exports nl.basjes.parse.useragent;
    exports nl.basjes.parse.useragent.analyze;
    exports nl.basjes.parse.useragent.analyze.treewalker;
    exports nl.basjes.parse.useragent.analyze.treewalker.steps;
    exports nl.basjes.parse.useragent.analyze.treewalker.steps.value;
    exports nl.basjes.parse.useragent.analyze.treewalker.steps.walk;
    exports nl.basjes.parse.useragent.analyze.treewalker.steps.walk.stepdown;
    exports nl.basjes.parse.useragent.analyze.treewalker.steps.lookup;
    exports nl.basjes.parse.useragent.analyze.treewalker.steps.compare;
    exports nl.basjes.parse.useragent.annotate;
    exports nl.basjes.parse.useragent.calculate;
    exports nl.basjes.parse.useragent.classify;
    exports nl.basjes.parse.useragent.config;
    exports nl.basjes.parse.useragent.debug;
    exports nl.basjes.parse.useragent.parse;
    exports nl.basjes.parse.useragent.utils;

    // Also generated code
    exports nl.basjes.parse.useragent.parser;

    // Optional: Allow reflection by Kryo
    requires static com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent                                         to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze                                 to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker                      to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker.steps                to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker.steps.value          to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker.steps.walk           to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker.steps.walk.stepdown  to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker.steps.lookup         to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.analyze.treewalker.steps.compare        to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.annotate                                ;//to com.esotericsoftware.kryo, nl.basjes.tests.parse.useragent;
    opens nl.basjes.parse.useragent.calculate                               to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.classify                                to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.config                                  to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.debug                                   to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.parse                                   to com.esotericsoftware.kryo;
    opens nl.basjes.parse.useragent.utils                                   to com.esotericsoftware.kryo;

    // Nullability annotatons
    requires static org.jetbrains.annotations;

    // Logging
    requires static org.apache.logging.log4j;
    requires org.slf4j;     // #SHADED : Only available with slf4j 2.x
    requires spring.jcl;    // #SHADED : == org.apache.commons.logging with Automatic module name

    requires com.github.benmanes.caffeine;          // Caching
    requires nl.basjes.collections.prefixmap;       // Lookup data structure

    // Automatic modules :(
    requires org.antlr.antlr4.runtime; // #SHADED : Shaded and relocated
    requires org.yaml.snakeyaml;       // #SHADED : Shaded and relocated
    requires java.logging;             // Needed for snakeyaml after being shaded
    requires spring.core;              // #SHADED : Shaded and relocated
    requires org.apache.commons.text;
    requires org.apache.commons.lang3;
    requires org.apache.httpcomponents.client5.httpclient5;

}
