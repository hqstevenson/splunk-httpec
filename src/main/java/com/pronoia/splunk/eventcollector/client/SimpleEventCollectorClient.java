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
package com.pronoia.splunk.eventcollector.client;

import com.pronoia.splunk.eventcollector.EventDeliveryException;

import java.io.IOException;

import java.lang.management.ManagementFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;


/**
 * Simple Client for sending JSON-formatted events to a single Splunk HTTP
 * Collector.
 */
public class SimpleEventCollectorClient extends AbstractEventCollectorClient implements SimpleEventCollectorClientMBean {
    static final int RETRY_COUNT = 3;
    static final long CONNECTION_TIME_TO_LIVE_MILLIS = 500;

    CloseableHttpClient httpClient;

    ObjectName clientObjectName;

    Date startTime;
    Date lastEventTime;
    Date stopTime;

    long eventCount;

    /**
     * Create a new EventCollectorClient.
     */
    public SimpleEventCollectorClient() {
    }

   /**
     * Determine if the instance has been intialized.
     *
     * @return true if the instance has been intialized; false otherwise
     */
    public boolean isInitialized() {
        return (httpClient == null) ? false : true;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public Date getLastEventTime() {
        return lastEventTime;
    }

    @Override
    public long getEventCount() {
        return eventCount;
    }

    @Override
    public Date getStopTime() {
        return stopTime;
    }

    public synchronized void initialize() {
        registerMBean();
        start();
    }

    public synchronized void destroy() {
        stop();
        unregisterMBean();
    }

    /**
     * Start the HTTP Event Collector client instance.
     */
    @Override
    public synchronized void start() {
        if (httpClient == null) {
            HttpClientBuilder clientBuilder = HttpClients.custom();
            clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT,true))
                    .setConnectionTimeToLive(CONNECTION_TIME_TO_LIVE_MILLIS, TimeUnit.MILLISECONDS);
            if (!eventCollectorInfo.isCertificateValidationEnabled()) {
                clientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
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
            startTime = new Date();
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
            try {
                httpClient.close();
            } catch (Exception closeEx) {
                log.info("Ignoring exception encountered closing the HTTP Client", closeEx);
            } finally {
                httpClient = null;
                stopTime = new Date();
            }
        } else {
            log.warn("{} is not initialized - ignoring 'destroy()' call", this.getClass().getSimpleName());
        }
    }

    /**
     * Restart the HTTP Event Collector client instance.
     */
    @Override
    public synchronized void restart() {
        stop();
        try {
            Thread.sleep(5000);
            start();
        } catch (InterruptedException interruptedEx) {
            log.warn("Restart was interrupted - consumer will not be restarted", interruptedEx);
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

        CloseableHttpResponse response = null;
        final HttpPost httpPost = new HttpPost(getPostUrl());

        httpPost.setHeader("Authorization", getAuthorizationHeaderValue());
        httpPost.setEntity(new StringEntity(event, ContentType.APPLICATION_JSON));
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
            } else {
                String responseBody = "<empty>";
                if (entity != null) {
                    responseBody = EntityUtils.toString(entity);
                }
                final String message = String.format("Post failed with response %s - %s for payload %s", response, responseBody, event);
                log.error(message);
                throw new EventDeliveryException(event, message);
            }
            lastEventTime = new Date();
            ++eventCount;
        } catch (IOException ioEx) {
            throw new EventDeliveryException(event, ioEx);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException closeEx) {
                    log.warn("Exception encountered closing HTTP response", closeEx);
                }
            }
        }
    }


    void registerMBean() {
        String newClientObjectNameString = String.format("com.pronoia.splunk.httpec:type=%s,id=%s",
            this.getClass().getSimpleName(), getClientId());

        try {
            clientObjectName = new ObjectName(newClientObjectNameString);
        } catch (MalformedObjectNameException malformedNameEx) {
            log.warn("Failed to create ObjectName for string {} - MBean will not be registered", newClientObjectNameString, malformedNameEx);
            return;
        }

        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, clientObjectName);
        } catch (InstanceAlreadyExistsException allreadyExistsEx) {
            log.warn("MBean already registered for name {}", clientObjectName, allreadyExistsEx);
        } catch (MBeanRegistrationException registrationEx) {
            log.warn("MBean registration failure for name {}", clientObjectName, registrationEx);
        } catch (NotCompliantMBeanException nonCompliantMBeanEx) {
            log.warn("Invalid MBean for name {}", clientObjectName, nonCompliantMBeanEx);
        }

    }

    void unregisterMBean() {
        if (clientObjectName != null) {
            try {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(clientObjectName);
            } catch (InstanceNotFoundException | MBeanRegistrationException unregisterEx) {
                log.warn("Failed to unregister consumer MBean {}", clientObjectName.getCanonicalName(), unregisterEx);
            } finally {
                clientObjectName = null;
            }
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
