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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import nl.basjes.parse.useragent.UserAgentAnalyzerDirect;
import nl.basjes.parse.useragent.UserAgentAnalyzerDirect.UserAgentAnalyzerDirectBuilder;

import java.nio.ByteBuffer;

class TestUserAgentAnalyzerDirectKryoSerialization extends AbstractAnalyzerSerializationTest<UserAgentAnalyzerDirect> {

    byte[] serialize(UserAgentAnalyzerDirect uaa) {
        Kryo             kryo             = new Kryo();
        UserAgentAnalyzerDirect.configureKryo(kryo);

        // For debugging
        kryo.setRegistrationRequired(true);
        kryo.setWarnUnregisteredClasses(true);

        ByteBufferOutput byteBufferOutput = new ByteBufferOutput(1_000_000, -1);
        kryo.writeClassAndObject(byteBufferOutput, uaa);

        ByteBuffer buf = byteBufferOutput.getByteBuffer();
        byte[] arr = new byte[buf.position()];
        buf.rewind();
        buf.get(arr);

        return arr;
    }

    UserAgentAnalyzerDirect deserialize(byte[] bytes) {
        Kryo            kryo            = new Kryo();
        UserAgentAnalyzerDirect.configureKryo(kryo);

        ByteBufferInput byteBufferInput = new ByteBufferInput(bytes);
        return (UserAgentAnalyzerDirect) kryo.readClassAndObject(byteBufferInput);
    }

    @Override
    UserAgentAnalyzerDirect create() {
        UserAgentAnalyzerDirectBuilder builder = UserAgentAnalyzerDirect.newBuilder();
        configureTestInstance(builder);
        return builder.build();
    }

}
