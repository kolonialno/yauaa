/*
 * Yet Another UserAgent Analyzer
 * Copyright (C) 2013-2017 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.basjes.parse.useragent.debug;

import nl.basjes.parse.useragent.analyze.Analyzer;
import nl.basjes.parse.useragent.analyze.MatcherAction;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;

public class FlattenPrinter implements Analyzer {

    PrintStream outputStream;

    public FlattenPrinter(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void inform(String path, String value, ParseTree ctx) {
        outputStream.println(path);
    }

    @Override
    public void informMeAbout(MatcherAction matcherAction, String keyPattern) {

    }
}
