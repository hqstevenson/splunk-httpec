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
package com.pronoia.splunk.itest.eventcollector.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import com.pronoia.splunk.eventcollector.client.SimpleEventCollectorClient;

import org.json.simple.JSONObject;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for the SimpleEventCollectorClient.
 */
public class SimpleEventCollectorClientIT {
    SimpleEventCollectorClient client;

    static void setClientInfo(SimpleEventCollectorClient genericClient) {
        genericClient.setPort(8088);

        // Local Settings
        genericClient.setHost("localhost");
        genericClient.setPort(18088);
//        genericClient.setAuthorizationToken("9952fa62-cef3-4509-badc-94a8f191dcb1");
        genericClient.setAuthorizationToken("5DA702AD-D855-4679-9CDE-A398494BE854");
        genericClient.setUseSSL(false);
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

//        payloadBuilder.put("index", "fuse-dev");
        payloadBuilder.put("index", "talend-jms");
//        payloadBuilder.put("event", "Hello");
        payloadBuilder.put("event", loadFile());

        String payload = payloadBuilder.toJSONString();

        client.sendEvent(payload);
    }

    /**
     * As long as the sendEvent method succeeds, this test passes.  Manual validation of the payload in Splunk is
     * required.
     *
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testPostComplexPayload() throws Exception {
        JSONObject payloadBuilder = new JSONObject();

        payloadBuilder.put("host", "lsdiesbap01");
        payloadBuilder.put("index", "fuse-dev");
        payloadBuilder.put("sourcetype", "hl7v2-message");
        // payloadBuilder.put("time", String.format("%.3f", System.currentTimeMillis() / 1000.0));

        JSONObject fieldsBuilder = new JSONObject();
        fieldsBuilder.put("container", "fred");

        // payloadBuilder.put("fields", fieldsBuilder);

        payloadBuilder.put("event", "TEST MESSAGE " + new Date());

        String payload = payloadBuilder.toJSONString();

        client.sendEvent(payload);
    }

    /*
    {
      "host": "lsdiesbap01",
      "index": "fuse-dev",
      "sourcetype": "hl7v2-msg",
      "source": "queue:\/\/audit.in",
      "time": "1498858945.148",
      "fields": {
        "container": "activemq_ancillary_inbound",
        "JMSPriority": "0",
        "JMSType": "org.apache.activemq.command.ActiveMQTextMessage",
        "JMSMessageID": "ID:lsdiesbap01-37162-1498856693513-19:1:1:1:1",
        "JMSDestination": "queue:\/\/audit.in",
        "JMSRedelivered": "false",
        "JMSTimestamp": "1498858945148",
        "JMSDeliveryMode": "2"
      },
      "event": "TEST MESSAGE"
    }
    */

    String loadFile() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("src/test/data/data.xml"));
        return new String(encoded);
    }
}
