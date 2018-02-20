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

import java.util.HashMap;
import java.util.Map;

import com.pronoia.splunk.eventcollector.stub.JacksonEventBuilderSupportStub;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the configuration methods of the EventBuilderSupport class.
 */
public class JacksonEventBuilderSupportConfigurationTest {
    JacksonEventBuilderSupport<String> instance;

    @Before
    public void setUp() throws Exception {
        instance = new JacksonEventBuilderSupportStub();
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetIndex() throws Exception {
        final String expected = "dummy index name";

        assertNull("Index should be null on creation", instance.index);
        instance.index = expected;

        assertEquals(expected, instance.getIndex());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetIndex() throws Exception {
        final String expected = "dummy index name";

        assertNull("Index should be null on creation", instance.index);
        instance.setIndex(expected);

        assertEquals(expected, instance.index);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetHost() throws Exception {
        final String expected = "dummy host name";

        assertNull("Host should be null on creation", instance.host);
        instance.host = expected;

        assertEquals(expected, instance.getHost());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetHost() throws Exception {
        final String expected = "dummy host name";

        assertNull("Host should be null on creation", instance.host);
        instance.setHost(expected);

        assertEquals(expected, instance.host);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetSource() throws Exception {
        final String expected = "dummy source";

        assertNull("Source should be null on creation", instance.source);
        instance.source = expected;

        assertEquals(expected, instance.getSource());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetSource() throws Exception {
        final String expected = "dummy source";

        assertNull("Source should be null on creation", instance.source);
        instance.setSource(expected);

        assertEquals(expected, instance.source);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetSourcetype() throws Exception {
        final String expected = "dummy sourcetype";

        assertNull("Sourcetype should be null on creation", instance.sourcetype);
        instance.sourcetype = expected;

        assertEquals(expected, instance.getSourcetype());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetSourcetype() throws Exception {
        final String expected = "dummy sourcetype";

        assertNull("Sourcetype should be null on creation", instance.sourcetype);
        instance.setSourcetype(expected);

        assertEquals(expected, instance.sourcetype);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetFields() throws Exception {
        final Map<String, Object> expected = new HashMap<>();

        assertNull("Fields map should be null on creation", instance.fields);
        instance.fields = expected;

        assertEquals(expected, instance.getFields());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetFields() throws Exception {
        Map<String, Object> expected = new HashMap<>();
        expected.put("myField", "myValue");

        assertNull("Fields map should be null on creation", instance.fields);
        instance.setFields(expected);

        assertEquals(expected, instance.fields);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetField() throws Exception {
        final String expectedFieldName = "fieldName";
        final String expectedFieldValue = "fieldValue";

        final Map<String, Object> expected = new HashMap<>();
        expected.put(expectedFieldName, expectedFieldValue);

        assertNull("Fields map should be null on creation", instance.fields);

        instance.setField(null, expectedFieldValue);
        assertTrue(instance.fields.isEmpty());

        instance.setField("", expectedFieldValue);
        assertTrue(instance.fields.isEmpty());

        instance.setField(expectedFieldName, null);
        assertTrue(instance.fields.isEmpty());

        instance.setField(expectedFieldName, "");
        assertTrue(instance.fields.isEmpty());

        instance.setField(expectedFieldName, expectedFieldValue);
        assertEquals(expected, instance.fields);

        instance.setField(expectedFieldName, null);
        assertTrue(instance.fields.isEmpty());

        instance.setField(expectedFieldName, expectedFieldValue);
        assertEquals(expected, instance.fields);

        instance.setField(expectedFieldName, "");
        assertTrue(instance.fields.isEmpty());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testClearFieldsWithNullMap() throws Exception {
        assertNull("Fields map should be null on creation", instance.fields);
        instance.clearFields();
        assertNull("Fields map should still be null", instance.fields);

        instance.setField("fieldName", null);
        assertNotNull("Fields map should not be null", instance.fields);

        instance.clearFields();
        assertNotNull("Fields map should still not be null", instance.fields);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testClearFieldsWithEmptyMap() throws Exception {
        assertNull("Fields map should be null on creation", instance.fields);
        instance.fields = new HashMap<>();

        instance.clearFields();

        assertNotNull("Fields map should not be null", instance.fields);
        assertTrue("Fields map should be empty", instance.fields.isEmpty());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testClearFields() throws Exception {
        assertNull("Fields map should be null on creation", instance.fields);
        instance.fields = new HashMap<>();
        instance.fields.put("fieldOne", "fieldValueOne");

        instance.clearFields();

        assertNotNull("Fields map should not be null", instance.fields);
        assertTrue("Fields map should be empty", instance.fields.isEmpty());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetTimestamp() throws Exception {
        final Double expected = 1491346209382L / 1000.0;

        assertNull("Timestamp should be null on creation", instance.timestamp);
        instance.timestamp = expected;

        assertEquals(expected, instance.getTimestamp());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetTimestamp() throws Exception {
        final Double expected = 1491346209382L / 1000.0;

        assertNull("Timestamp should be null on creation", instance.timestamp);
        instance.setTimestamp(expected);

        assertEquals(expected, instance.timestamp);
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testGetEventBody() throws Exception {
        final String expected = "Dummy Event";

        assertNull("Event body should be null on creation", instance.eventBody);
        instance.eventBody = expected;

        assertEquals(expected, instance.getEventBody());
    }

    /**
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testSetEventBody() throws Exception {
        final String expected = "Dummy Event";

        assertNull("Event body should be null on creation", instance.eventBody);
        instance.setEventBody(expected);

        assertEquals(expected, instance.eventBody);
    }

}
