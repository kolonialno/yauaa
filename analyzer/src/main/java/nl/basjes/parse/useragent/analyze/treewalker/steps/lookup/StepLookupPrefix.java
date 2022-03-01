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

package nl.basjes.parse.useragent.analyze.treewalker.steps.lookup;

import nl.basjes.collections.PrefixMap;
import nl.basjes.collections.prefixmap.StringPrefixMap;
import nl.basjes.parse.useragent.analyze.treewalker.steps.Step;
import nl.basjes.parse.useragent.analyze.treewalker.steps.WalkList.WalkResult;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class StepLookupPrefix extends Step {

    private final String            lookupName;
    private final String            defaultValue;
    private final PrefixMap<String> prefixMap;
    private final boolean           canFail;

    @SuppressWarnings("unused") // Private constructor for serialization systems ONLY (like Kryo)
    private StepLookupPrefix() {
        lookupName = null;
        defaultValue = null;
        prefixMap = null;
        canFail = true;
    }

    public StepLookupPrefix(String lookupName, Map<String, String> prefixList, String defaultValue) {
        this.lookupName = lookupName;
        this.defaultValue = defaultValue;
        this.prefixMap = new StringPrefixMap<>(false);
        this.prefixMap.putAll(prefixList);
        canFail = defaultValue == null;
    }

    @Override
    public boolean canFail() {
        return canFail;
    }

    @Override
    public WalkResult walk(@NotNull ParseTree tree, @Nullable String value) {
        String actualValue = getActualValue(tree, value);

        String result = prefixMap.getLongestMatch(actualValue);

        if (result == null) {
            if (defaultValue == null) {
                return null;
            } else {
                return walkNextStep(tree, defaultValue);
            }
        }
        return walkNextStep(tree, result);
    }


    @Override
    public String toString() {
        return "LookupPrefix(@" + lookupName + " ; default="+defaultValue+")";
    }

}
