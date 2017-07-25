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

import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.EventCollectorInfo;
import com.pronoia.splunk.eventcollector.EventDeliveryException;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Client for sending JSON-formatted events to a single Splunk HTTP
 * Collector.
 */
public class SimpleEventCollectorClient implements EventCollectorClient {
  static final int RETRY_COUNT = 3;
  static final long CONNECTION_TIME_TO_LIVE_MILLIS = 500;

  Logger log = LoggerFactory.getLogger(this.getClass());
  EventCollectorInfo eventCollectorInfo = new EventCollectorInfo();
  HttpClient httpClient;

  /**
   * Create a new EventCollectorClient.
   */
  public SimpleEventCollectorClient() {
  }

  /**
   * Get the hostname or IP address of the Splunk HTTP Event Collector.
   *
   * @return hostname or IP
   */
  public String getHost() {
    return eventCollectorInfo.getHost();
  }

  /**
   * Set the hostname or IP address of the Splunk HTTP Event Collector.
   *
   * @param host hostname or IP
   */
  public void setHost(final String host) {
    eventCollectorInfo.setHost(host);
  }

  /**
   * Get the port of the Splunk HTTP Event Collector.
   *
   * @return port
   */
  public Integer getPort() {
    return eventCollectorInfo.getPort();
  }

  /**
   * Set the port of the Splunk HTTP Event Collector.
   *
   * @param port port
   */
  public void setPort(final Integer port) {
    eventCollectorInfo.setPort(port);
  }

  /**
   * Get the authorization token for Splunk HTTP Event Collector.
   *
   * @return the Splunk Authorization Token
   */
  public String getAuthorizationToken() {
    return eventCollectorInfo.getAuthorizationToken();
  }

  /**
   * Set the authorization token for Splunk HTTP Event Collector.
   *
   * @param authorizationToken the Splunk Authorization Token
   */
  public void setAuthorizationToken(final String authorizationToken) {
    eventCollectorInfo.setAuthorizationToken(authorizationToken);
  }

  /**
   * Get the URL Splunk HTTP Event Collector.
   *
   * @return HTTP POST URL as a String
   */
  public String getPostUrl() {
    return eventCollectorInfo.getPostUrl();
  }

  /**
   * Get the value that will be used for the HTTP Authorization header.
   *
   * @return HTTP Authorization header value
   */
  public String getAuthorizationHeaderValue() {
    return eventCollectorInfo.getAuthorizationHeaderValue();
  }

  /**
   * Determine if SSL certificate validation is enabled.
   *
   * @return true if certificate validation is enabled; false otherwise
   */
  public boolean isCertificateValidationEnabled() {
    return eventCollectorInfo.isCertificateValidationEnabled();
  }

  /**
   * Enable/Disable SSL certificate validation.
   *
   * @param validateCertificates if true certificate validation is enabled; otherwise certificate
   *                             validation is disabled
   */
  public void setValidateCertificates(final boolean validateCertificates) {
    eventCollectorInfo.setValidateCertificates(validateCertificates);
  }

  /**
   * Disable SSL certificate validation.
   */
  public void disableCertificateValidation() {
    eventCollectorInfo.disableCertificateValidation();
  }

  /**
   * Enable SSL certificate validation.
   */
  public void enableCertificateValidation() {
    eventCollectorInfo.enableCertificateValidation();
  }

  /**
   * Determine if the instance has been intialized.
   *
   * @return true if the instance has been intialized; false otherwise
   */
  public boolean isInitialized() {
    return (httpClient == null) ? false : true;
  }

  /**
   * Start the HTTP Event Collector client instance.
   */
  @Override
  public synchronized void start() {
    if (httpClient == null) {
      HttpClientBuilder clientBuilder = HttpClients.custom();
      clientBuilder
          .setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT,
              true))
          .setConnectionTimeToLive(CONNECTION_TIME_TO_LIVE_MILLIS, TimeUnit.MILLISECONDS);
      if (!eventCollectorInfo.isCertificateValidationEnabled()) {
        clientBuilder
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        try {
          SSLContext sslContext = new SSLContextBuilder()
              .loadTrustMaterial(null, new AcceptAllTrustStrategy())
              .build();
          clientBuilder.setSSLContext(sslContext);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException ex) {
          throw new IllegalStateException("Failed to create SSL Context", ex);
        }

      }

      httpClient = clientBuilder.build();
    } else {
      log.warn("{} already initialized - ignoring 'initialize()' call", this.getClass().getSimpleName());
    }


  }

  /**
   * Stop the HTTP Event Collector client instance.
   */
  @Override
  public synchronized void stop() {
    if (httpClient != null) {
      httpClient = null;
    } else {
      log.warn("{} is not initialized - ignoring 'destroy()' call", this.getClass().getSimpleName());
    }
  }

  /**
   * Send and event to the the HTTP Event Collector client.
   */
  @Override
  public void sendEvent(final String event) throws EventDeliveryException {
    log.debug("Posting payload to {}: {}", getPostUrl(), event);

    if (httpClient == null) {
      start();
    }

    HttpResponse response = null;
    final HttpPost httpPost = new HttpPost(getPostUrl());

    httpPost.setHeader("Authorization", getAuthorizationHeaderValue());
    httpPost.setEntity(new StringEntity(event, ContentType.APPLICATION_JSON));
    try {
      response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        InputStream stream = entity.getContent();
        stream.close();
      }
      if (response.getStatusLine().getStatusCode() != 200) {
        log.error("Post failed with response {} for payload {}", response, event);
        throw new EventDeliveryException(event, response.toString());
      }
    } catch (IOException ioEx) {
      throw new EventDeliveryException(event, ioEx);
    }
  }

  /**
   * Simple implementation of a TrustStrategy that does not validate
   * certificates (it accepts all certificates as valid).
   *
   * <p>This class is primarily for use in development environments, and will be
   * use when the disableCertificateValidation attribute is true.
   */
  class AcceptAllTrustStrategy implements TrustStrategy {
    @Override
    public boolean isTrusted(final X509Certificate[] x509Certificates,
                             final String s) throws CertificateException {
      return true;
    }
  }
}
