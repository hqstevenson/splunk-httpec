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

package com.pronoia.splunk.eventcollector.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the SimpleEventCollectorClient lifecycle.
 */
public class SimpleEventCollectorClientTest {
  SimpleEventCollectorClient client;

  @Before
  public void setUp() throws Exception {
    client = new SimpleEventCollectorClient();
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testStartAndStop() throws Exception {
    assertFalse("Default value should be false", client.isInitialized());

    client.start();

    assertTrue(client.isInitialized());

    HttpClient tmpClient = client.httpClient;

    client.start();
    assertTrue(client.isInitialized());
    assertSame("Object should not have changed", tmpClient, client.httpClient);

    client.stop();
    assertFalse(client.isInitialized());
    assertNull(client.httpClient);

    client.stop();
    assertFalse(client.isInitialized());
    assertNull(client.httpClient);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testStopWhenUninitialized() throws Exception {
    assertFalse("Default value should be false", client.isInitialized());

    client.stop();
    assertFalse(client.isInitialized());
    assertNull(client.httpClient);
  }

}
