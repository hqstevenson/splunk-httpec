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

package com.pronoia.splunk.itest.eventcollector.client;

import com.pronoia.splunk.eventcollector.client.SimpleEventCollectorClient;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests for the SimpleEventCollectorClient.
 */
public class SimpleEventCollectorClientIT {
  Logger log = LoggerFactory.getLogger(this.getClass());

  SimpleEventCollectorClient client;

  static void setClientInfo(SimpleEventCollectorClient genericClient) {
    genericClient.setPort(8088);

    genericClient.setHost("localhost");
    genericClient.setAuthorizationToken("11ABA1B0-CF99-4E8A-8131-A162CBB32163");
    genericClient.disableCertificateValidation();
  }

  @Before
  public void setUp() throws Exception {
    client = new SimpleEventCollectorClient();
    setClientInfo(client);
  }

  /**
   * As long as the sendEvent method succeeds, this test passes.  Manual validation of the payload in Splunk is
   * required.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testPostPayload() throws Exception {
    JSONObject payloadBuilder = new JSONObject();

    // payloadBuilder.put("index", "main");
    payloadBuilder.put("event", "Hello");

    String payload = payloadBuilder.toJSONString();

    client.sendEvent(payload);
  }

}
