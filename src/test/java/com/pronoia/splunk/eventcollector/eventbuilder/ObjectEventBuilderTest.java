/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pronoia.splunk.eventcollector.eventbuilder;

import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.stub.EventCollectorClientStub;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test the ObjectEventBuilder.
 */
public class ObjectEventBuilderTest {
    static final long TEST_TIMESTAMP_IN_MILLISECONDS = 1491346209382L;
    static final double MILLISECONDS_PER_SECOND = 1000.0;

    EventCollectorClient clientStub = new EventCollectorClientStub();
    ObjectEventBuilder instance;

    @Before
    public void setUp() throws Exception {
        instance = new ObjectEventBuilder();
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testBuildString() throws Exception {
        // @formatter:off
        final String expected
                = "{"
                +     "\"host\":\"dummy-host\","
                +     "\"index\":\"dummy-index\","
                +     "\"source\":\"dummy-source\","
                +     "\"sourcetype\":\"dummy-sourcetype\","
                +     "\"time\":\"1491346209.382\","
                +     "\"fields\":{"
                +         "\"fieldOne\":\"fieldOneValue\","
                +         "\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueOne\"]"
                +     "},"
                +     "\"event\":\"Dummy Event Body\""
                + "}";
        // @formatter:on

        instance
            .index("dummy-index")
            .host("dummy-host")
            .source("dummy-source")
            .sourcetype("dummy-sourcetype")
            .field("fieldOne", "fieldOneValue")
            .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueOne")
            .timestamp(TEST_TIMESTAMP_IN_MILLISECONDS / MILLISECONDS_PER_SECOND)
            .eventBody("Dummy Event Body");


        assertEquals(expected, instance.build(clientStub));
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testBuildMap() throws Exception {
        // @formatter:off
        final String expected
                = "{"
                +     "\"host\":\"dummy-host\","
                +     "\"index\":\"dummy-index\","
                +     "\"source\":\"dummy-source\","
                +     "\"sourcetype\":\"dummy-sourcetype\","
                +     "\"time\":\"1491346209.382\","
                +     "\"fields\":{"
                +         "\"fieldOne\":\"fieldOneValue\","
                +         "\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueOne\"]"
                +     "},"
                +     "\"event\":{"
                +         "\"keyOne\":\"valueOne\","
                +         "\"keyTwo\":\"valueTwo\""
                +     "}"
                + "}";
        // @formatter:on

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("keyOne", "valueOne");
        body.put("keyTwo", "valueTwo");

        instance
            .index("dummy-index")
            .host("dummy-host")
            .source("dummy-source")
            .sourcetype("dummy-sourcetype")
            .field("fieldOne", "fieldOneValue")
            .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueOne")
            .timestamp(TEST_TIMESTAMP_IN_MILLISECONDS / MILLISECONDS_PER_SECOND);

        instance.eventBody(body);


        assertEquals(expected, instance.build(clientStub));
    }
}
