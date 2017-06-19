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

package com.pronoia.splunk.eventcollector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the EventCollectorInfo value object class.
 */
public class EventCollectorInfoTest {
  EventCollectorInfo instance;

  @Before
  public void setUp() throws Exception {
    instance = new EventCollectorInfo();
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetHost() throws Exception {
    String expected = "dummy.host.local";

    assertNull("Default value should be null", instance.host);
    assertNull("Default value should be null", instance.getHost());
    instance.host = expected;

    assertEquals(expected, instance.getHost());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSetHost() throws Exception {
    String expected = "dummy.host.local";

    assertNull("Default value should be null", instance.host);
    assertNull("Default value should be null", instance.cachedPostUrl);
    instance.setHost(expected);

    assertEquals(expected, instance.host);
    assertNull("Value should still be null", instance.cachedPostUrl);

    instance.getPostUrl();
    assertEquals(expected, instance.host);
    assertNotNull("Value should not be null anymore", instance.cachedPostUrl);

    instance.setHost(expected);

    assertEquals(expected, instance.host);
    assertNull("Value should be be null again", instance.cachedPostUrl);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetPort() throws Exception {
    final Integer expected = 1234;

    assertNull("Default value should be null", instance.port);
    assertNull("Default value should be null", instance.getPort());
    instance.port = expected;

    assertEquals(expected, instance.getPort());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSetPort() throws Exception {
    final Integer expected = 1234;

    assertNull("Default value should be null", instance.port);
    assertNull("Default value should be null", instance.cachedPostUrl);
    instance.setPort(expected);

    assertEquals(expected, instance.port);
    assertNull("Value should still be null", instance.cachedPostUrl);

    instance.getPostUrl();
    assertEquals(expected, instance.port);
    assertNotNull("Value should not be null anymore", instance.cachedPostUrl);

    instance.setPort(expected);

    assertEquals(expected, instance.port);
    assertNull("Value should be be null again", instance.cachedPostUrl);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetAuthorizationToken() throws Exception {
    String expected = "A-B-C-D";

    assertNull("Default value should be null", instance.authorizationToken);
    assertNull("Default value should be null", instance.getAuthorizationToken());
    instance.authorizationToken = expected;

    assertEquals(expected, instance.getAuthorizationToken());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSetAuthorizationToken() throws Exception {
    String expected = "A-B-C-D";

    assertNull("Default value should be null", instance.authorizationToken);
    instance.setAuthorizationToken(expected);

    assertEquals(expected, instance.authorizationToken);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testIsCertificateValidationEnabled() throws Exception {
    assertTrue("Default value should be true", instance.validateCertificates);
    assertTrue(instance.isCertificateValidationEnabled());

    instance.validateCertificates = false;
    assertFalse(instance.isCertificateValidationEnabled());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testSetDisableCertificateValidation() throws Exception {
    assertTrue("Default value should be true", instance.validateCertificates);

    instance.setValidateCertificates(false);
    assertFalse(instance.validateCertificates);

    instance.setValidateCertificates(true);
    assertTrue(instance.validateCertificates);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testDisableCertificateValidation() throws Exception {
    assertTrue("Default value should be true", instance.validateCertificates);

    instance.disableCertificateValidation();
    assertFalse("Should now be false", instance.validateCertificates);

    instance.disableCertificateValidation();
    assertFalse("Should still be false", instance.validateCertificates);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testEnableCertificateValidation() throws Exception {
    assertTrue("Default value should be true", instance.validateCertificates);

    instance.validateCertificates = false;

    instance.enableCertificateValidation();
    assertTrue("Should now be true", instance.validateCertificates);

    instance.enableCertificateValidation();
    assertTrue("Should still be true", instance.validateCertificates);
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetPostUrl() throws Exception {
    final String expectedHost = "dummy.host.local";
    final int expectedPort = 1234;
    String expected = String.format("https://%s:%d/services/collector",
        expectedHost, expectedPort);

    instance.host = expectedHost;
    instance.port = expectedPort;

    assertEquals(expected, instance.getPostUrl());
  }

  /**
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetAuthorizationHeaderValue() throws Exception {
    String expected = "Splunk A-B-C-D";

    instance.authorizationToken = "A-B-C-D";

    assertEquals(expected, instance.getAuthorizationHeaderValue());
  }

}
