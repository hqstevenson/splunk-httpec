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

package com.pronoia.splunk.eventcollector.eventbuilder;

import static org.junit.Assert.assertEquals;

import com.pronoia.splunk.eventcollector.stub.JacksonEventBuilderSupportStub;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Test JSON serialization for the EventBuilderSupport class.
 */
public class JacksonEventBuilderSupportTest {
  JacksonEventBuilderSupport<String> instance;

  Double timestamp = 1505323567.566;

  @Before
  public void setUp() throws Exception {
    instance = new JacksonEventBuilderSupportStub();
    instance.timestamp(timestamp);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithIndex() throws Exception {
    final String expected = "{"
        + "\"host\":\"" + instance.getDefaultHost() + "\","
        + "\"index\":\"dummy-index\","
        + "\"time\":\"1505323567.566\""
        + "}";

    JSONObject tmpObject = new JSONObject();

    instance.index("dummy-index");

    instance.addDefaultFieldsToMap(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithHost() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.host("dummy-host");

    instance.addDefaultFieldsToMap(tmpObject);

    assertEquals("{\"host\":\"dummy-host\",\"time\":\"1505323567.566\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithSource() throws Exception {
    final String expected = "{"
        + "\"host\":\"" + instance.getDefaultHost() + "\","
        + "\"source\":\"dummy-source\","
        + "\"time\":\"1505323567.566\""
        + "}";

    JSONObject tmpObject = new JSONObject();

    instance.source("dummy-source");

    instance.addDefaultFieldsToMap(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithSourcetype() throws Exception {
    final String expected = "{"
        + "\"host\":\"" + instance.getDefaultHost() + "\","
        + "\"sourcetype\":\"dummy-sourcetype\","
        + "\"time\":\"1505323567.566\""
        + "}";

    JSONObject tmpObject = new JSONObject();

    instance.sourcetype("dummy-sourcetype");

    instance.addDefaultFieldsToMap(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithFields() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance
        .field("fieldOne", "fieldOneValueOne")
        .field("fieldTwo", "fieldTwoValueOne");

    instance.addAdditionalFieldsToMap(tmpObject);

    assertEquals("{\"fields\":{\"fieldOne\":\"fieldOneValueOne\",\"fieldTwo\":\"fieldTwoValueOne\"}}", tmpObject
        .toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithMultivaluedFields() throws Exception {
    final String expected = "{"
        + "\"host\":\"" + instance.getDefaultHost() + "\","
        + "\"time\":\"1505323567.566\","
        + "\"fields\":{"
        +   "\"fieldOne\":[\"fieldOneValueOne\",\"fieldOneValueTwo\"],"
        +   "\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueTwo\"]"
        +   "}"
        + "}";

    JSONObject tmpObject = new JSONObject();

    instance
        .field("fieldOne", "fieldOneValueOne", "fieldOneValueTwo")
        .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueTwo");

    instance.addDefaultFieldsToMap(tmpObject);
    instance.addAdditionalFieldsToMap(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeFieldsWithTimestamp() throws Exception {
    final String expected = "{"
        + "\"host\":\"" + instance.getDefaultHost() + "\","
        + "\"time\":\"1491346209.382\""
        + "}";

    final long testTimestampInMilliseconds = 1491346209382L;
    final double millisecondsPerSecond = 1000.0;

    JSONObject tmpObject = new JSONObject();

    instance.timestamp(testTimestampInMilliseconds / millisecondsPerSecond);

    instance.addDefaultFieldsToMap(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
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
        + "\"time\":\"1505323567.566\","
        + "\"fields\":{\"fieldOne\":\"fieldOneValue\",\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueTwo\"]}"
        + "}";

    JSONObject tmpObject = new JSONObject();

    instance
        .index("dummy-index")
        .host("dummy-host")
        .source("dummy-source")
        .sourcetype("dummy-sourcetype")
        .field("fieldOne", "fieldOneValue")
        .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueTwo");

    instance.addDefaultFieldsToMap(tmpObject);
    instance.addAdditionalFieldsToMap(tmpObject);

    assertEquals(expected, tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeBodyWithNullEventBody() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.addEventBodyToMap(tmpObject);

    assertEquals("{\"event\":\"null event body\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeBodyWithEmptyEventBody() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.eventBody("");
    instance.addEventBodyToMap(tmpObject);

    assertEquals("{\"event\":\"\"}", tmpObject.toJSONString());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSerializeBody() throws Exception {
    JSONObject tmpObject = new JSONObject();

    instance.eventBody("Dummy Event Body");
    instance.addEventBodyToMap(tmpObject);

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
    final String expected =
        "{"
            + "\"host\":\"dummy-host\","
            + "\"index\":\"dummy-index\","
            + "\"source\":\"dummy-source\","
            + "\"sourcetype\":\"dummy-sourcetype\","
            + "\"time\":\"1505323567.566\","
            + "\"fields\":{"
            + "\"fieldOne\":\"fieldOneValue\","
            + "\"fieldTwo\":[\"fieldTwoValueOne\",\"fieldTwoValueTwo\"]"
            + "},"
            + "\"event\":\"Dummy Event Body\""
            + "}";

    instance
        .index("dummy-index")
        .host("dummy-host")
        .source("dummy-source")
        .sourcetype("dummy-sourcetype")
        .field("fieldOne", "fieldOneValue")
        .field("fieldTwo", "fieldTwoValueOne", "fieldTwoValueTwo")
        .eventBody("Dummy Event Body");


    assertEquals(expected, instance.build());
  }

}
