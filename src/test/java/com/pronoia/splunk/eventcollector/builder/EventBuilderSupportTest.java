/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pronoia.splunk.eventcollector.builder;

import static org.junit.Assert.assertEquals;

import com.pronoia.splunk.eventcollector.EventBuilder;
import com.pronoia.splunk.eventcollector.stub.EventBuilderSupportStub;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Test JSON serialization for the EventBuilderSupport class.
 */
public class EventBuilderSupportTest {
  EventBuilderSupport<String> instance;

  @Before
  public void setUp() throws Exception {
    instance = new EventBuilderSupportStub();
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithIndex() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.index("dummy-index");

    instance.serializeFields(tmpObject);

    assertEquals("{\"index\":\"dummy-index\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithHost() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.host("dummy-host");

    instance.serializeFields(tmpObject);

    assertEquals("{\"host\":\"dummy-host\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithSource() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.source("dummy-source");

    instance.serializeFields(tmpObject);

    assertEquals("{\"source\":\"dummy-source\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithSourcetype() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.sourcetype("dummy-sourcetype");

    instance.serializeFields(tmpObject);

    assertEquals("{\"sourcetype\":\"dummy-sourcetype\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithFields() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance
        .field("fieldOne", "fieldOneValue")
        .field("fieldTwo", "fieldTwoValueOne");

    instance.serializeFields(tmpObject);

    assertEquals("{\"fields\":{\"fieldTwo\":\"fieldTwoValueOne\",\"fieldOne\":\"fieldOneValue\"}}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithMultivaluedFields() throws Exception {
    final String expected = "{\"fields\":{"
        + "\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueTwo\"],"
        + "\"fieldOne\":[\"fieldOneValueOne\",\"fieldOneValueTwo\"]}"
        + "}";
    JSONObject tmpObject = new JSONObject();

    instance
        .field("fieldOne", "fieldOneValueOne", "fieldOneValueTwo")
        .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueTwo");

    instance.serializeFields(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithTimestamp() throws Exception {
    final long testTimestampInMilliseconds = 1491346209382L;
    final double millisecondsPerSecond = 1000.0;

    JSONObject tmpObject = new JSONObject();

    instance.timestamp(testTimestampInMilliseconds / millisecondsPerSecond);

    instance.serializeFields(tmpObject);

    assertEquals("{\"time\":\"1491346209.382\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFields() throws Exception {
    final String expected = "{"
        + "\"host\":\"dummy-host\","
        + "\"index\":\"dummy-index\","
        + "\"sourcetype\":\"dummy-sourcetype\","
        + "\"source\":\"dummy-source\","
        + "\"fields\":{\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueOne\"],\"fieldOne\":\"fieldOneValue\"}"
        + "}";

    JSONObject tmpObject = new JSONObject();

    instance
        .index("dummy-index")
        .host("dummy-host")
        .source("dummy-source")
        .sourcetype("dummy-sourcetype")
        .field("fieldOne", "fieldOneValue")
        .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueOne");

    instance.serializeFields(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeBodyWithNullEventBody() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.serializeBody(tmpObject);

    assertEquals("{\"event\":null}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeBodyWithEmptyEventBody() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.event("");
    instance.serializeBody(tmpObject);

    assertEquals("{\"event\":\"\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeBody() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.event("Dummy Event Body");
    instance.serializeBody(tmpObject);

    assertEquals("{\"event\":\"Dummy Event Body\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testPutIfValueIsNotNullWithNullKey() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.putIfValueIsNotNull(tmpObject, null, "dummyValue");

    assertEquals("{}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testPutIfValueIsNotNullWithEmptyKey() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.putIfValueIsNotNull(tmpObject, "", "dummyValue");

    assertEquals("{}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testPutIfValueIsNotNullWithNullValue() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.putIfValueIsNotNull(tmpObject, "dummyKey", null);

    assertEquals("{}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testPutIfValueIsNotNullWithEmptyValue() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.putIfValueIsNotNull(tmpObject, "dummyKey", "");

    assertEquals("{\"dummyKey\":\"\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testPutIfValueIsNotNull() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.putIfValueIsNotNull(tmpObject, "dummyKey", "dummy Value");

    assertEquals("{\"dummyKey\":\"dummy Value\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testBuild() throws Exception {
    final long testEpochMilliseconds = 1491346209382L;
    final double millisecondsPerSecond = 1000.0;

    final String expected =
        "{"
            + "\"host\":\"dummy-host\","
            + "\"index\":\"dummy-index\","
            + "\"sourcetype\":\"dummy-sourcetype\","
            + "\"source\":\"dummy-source\","
            + "\"time\":\"1491346209.382\","
            + "\"fields\":{"
            + "\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueOne\"],"
            + "\"fieldOne\":\"fieldOneValue\""
            + "},"
            + "\"event\":\"Dummy Event Body\""
            + "}";

    instance
        .index("dummy-index")
        .host("dummy-host")
        .source("dummy-source")
        .sourcetype("dummy-sourcetype")
        .field("fieldOne", "fieldOneValue")
        .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueOne")
        .timestamp(testEpochMilliseconds / millisecondsPerSecond)
        .event("Dummy Event Body");


    assertEquals(expected, instance.build());
  }

}
