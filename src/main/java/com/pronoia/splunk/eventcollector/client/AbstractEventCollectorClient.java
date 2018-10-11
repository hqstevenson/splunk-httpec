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

import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.EventCollectorInfo;
import com.pronoia.splunk.eventcollector.EventDeliveryException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simple Client for sending JSON-formatted events to a single Splunk HTTP
 * Collector.
 */
public abstract class AbstractEventCollectorClient implements EventCollectorClient {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    EventCollectorInfo eventCollectorInfo = new EventCollectorInfo();

    static AtomicInteger clientCounter = new AtomicInteger(1);

    String clientId;

    String eventHost;
    String eventIndex;
    String eventSource;
    String eventSourcetype;

    Map<String, String> constantFields = new TreeMap<>();
    Map<String, String> includedSystemProperties = new TreeMap<>();
    Map<String, String> includedEnvironmentVariables = new TreeMap<>();

    /**
     * Create a new EventCollectorClient.
     */
    protected AbstractEventCollectorClient() {
    }

    @Override
    public String getClientId() {
        if (clientId == null || clientId.isEmpty()) {
            clientId = String.format("splunk-client-%d", clientCounter.getAndIncrement());
        }
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean hasEventHost() {
        return checkString(eventHost);
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public boolean hasEventIndex() {
        return checkString(eventIndex);
    }

    public String getEventIndex() {
        return eventIndex;
    }

    public void setEventIndex(String eventIndex) {
        this.eventIndex = eventIndex;
    }

    public boolean hasEventSource() {
        return checkString(eventSource);
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public boolean hasEventSourcetype() {
        return checkString(eventSourcetype);
    }

    public String getEventSourcetype() {
        return eventSourcetype;
    }

    public void setEventSourcetype(String eventSourcetype) {
        this.eventSourcetype = eventSourcetype;
    }

    @Override
    public boolean hasConstantFields() {
        return checkMap(constantFields);
    }

    @Override
    public Map<String, String> getConstantFields() {
        return getConstantFields(true);
    }

    @Override
    public Map<String, String> getConstantFields(boolean copy) {
        return copy ? createCopyOfMap(constantFields) : constantFields;
    }

    @Override
    public boolean hasIncludedSystemProperties() {
        return checkMap(includedSystemProperties);
    }

    @Override
    public Map<String, String> getIncludedSystemProperties() {
        return getIncludedSystemProperties(true);
    }

    @Override
    public Map<String, String> getIncludedSystemProperties(boolean copy) {
        return copy ? createCopyOfMap(includedSystemProperties) : includedSystemProperties;
    }

    @Override
    public boolean hasIncludedEnvironmentVariables() {
        return checkMap(includedEnvironmentVariables);
    }

    @Override
    public Map<String, String> getIncludedEnvironmentVariables() {
        return getIncludedEnvironmentVariables(true);
    }

    @Override
    public Map<String, String> getIncludedEnvironmentVariables(boolean copy) {
        return copy ? createCopyOfMap(includedEnvironmentVariables) : includedEnvironmentVariables;
    }

    public boolean isUseSSL() {
        return eventCollectorInfo.isUseSSL();
    }

    public void setUseSSL(boolean useSSL) {
        this.eventCollectorInfo.setUseSSL(useSSL);
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
     * Set the constant Splunk fields values to include in the event.
     *
     * @param constantFields a Map of Splunk field names and their associated values.
     */
    public void setConstantFields(Map<String, String> constantFields) {
        if (this.constantFields == null) {
            this.constantFields = new TreeMap<>();
        } else {
            this.constantFields.clear();
        }

        addConstantFields(constantFields);
    }

    /**
     * Add constant Splunk fields and their associated values to the current set of constant Splunk fields to include in the event.
     *
     * @param constantFields a Map of Splunk field names and their associated values.
     */
    public void addConstantFields(Map<String, String> constantFields) {
        if (this.constantFields == null) {
            this.constantFields = new TreeMap<>();
        }
        if (constantFields != null && !constantFields.isEmpty()) {
            this.constantFields.putAll(constantFields);
        }
    }

    /**
     * Add a constant Splunk field and value to the current set of constant Splunk fields to include in the event.
     *
     * @param splunkFieldName the Splunk field name
     * @param splunkFieldValue  the value for the field
     */
    public void addConstantField(String splunkFieldName, String splunkFieldValue) {
        if (this.constantFields == null) {
            this.constantFields = new TreeMap<>();
        }

        this.constantFields.put(splunkFieldName, splunkFieldValue);
    }

    /**
     * Set the system properties to include in the event.
     *
     * @param systemProperties a List of system property keys
     */
    public void setIncludedSystemProperties(List<String> systemProperties) {
        if (includedSystemProperties == null) {
            includedSystemProperties = new TreeMap<>();
        } else {
            includedSystemProperties.clear();
        }

        addIncludedSystemProperties(systemProperties);
    }

    /**
     * Set the system properties and their associated Splunk field names to include in the event.
     *
     * @param systemProperties a Map of system property keys and their associated Splunk field names.
     */
    public void setIncludedSystemProperties(Map<String, String> systemProperties) {
        if (includedSystemProperties == null) {
            includedSystemProperties = new TreeMap<>();
        } else {
            includedSystemProperties.clear();
        }

        addIncludedSystemProperties(systemProperties);
    }

    /**
     * Add system properties and their associated Splunk field names to the current set of system properties to include in the event.
     *
     * @param systemProperties a Map of system property keys and their associated Splunk field names.
     */
    public void addIncludedSystemProperties(Map<String, String> systemProperties) {
        if (includedSystemProperties == null) {
            includedSystemProperties = new TreeMap<>();
        }
        if (systemProperties != null && !systemProperties.isEmpty()) {
            includedSystemProperties.putAll(systemProperties);
        }
    }

    /**
     * Add system properties to the current set of system properties to include in the event.
     *
     * @param systemProperties a List of system property keys
     */
    public void addIncludedSystemProperties(List<String> systemProperties) {
        if (includedSystemProperties == null) {
            includedSystemProperties = new TreeMap<>();
        }
        if (systemProperties != null && !systemProperties.isEmpty()) {
            for (String systemProperty : systemProperties) {
                includeSystemProperty(systemProperty);
            }
        }
    }

    /**
     * Add a system properties and it's associated Splunk field name to the current set of system properties to include in the event.
     *
     * @param systemProperty a system property key
     * @param splunkFieldName the Splunk field name for the system property
     */
    public void includeSystemProperty(String systemProperty, String splunkFieldName) {
        if (includedSystemProperties == null) {
            includedSystemProperties = new TreeMap<>();
        }

        if (splunkFieldName != null && !splunkFieldName.isEmpty()) {
            includedSystemProperties.put(systemProperty, splunkFieldName);
        } else {
            includedSystemProperties.put(systemProperty, null);
        }
    }

    /**
     * Add a system property to the current set of system properties to include in the event.
     *
     * @param systemProperty a system property key
     */
    public void includeSystemProperty(String systemProperty) {
        if (includedSystemProperties == null) {
            includedSystemProperties = new TreeMap<>();
        }

        includedSystemProperties.put(systemProperty, null);
    }

    /**
     * Set the environment variables to include in the event.
     *
     * @param environmentVariables a List of environment variables
     */
    public void setIncludedEnvironmentVariables(List<String> environmentVariables) {
        if (includedEnvironmentVariables == null) {
            includedEnvironmentVariables = new TreeMap<>();
        } else {
            includedEnvironmentVariables.clear();
        }

        addIncludedEnvironmentVariables(environmentVariables);
    }

    /**
     * Set the environment variables and their associated Splunk field names to include in the event.
     *
     * @param environmentVariables a Map of environment variables and their associated Splunk field names.
     */
    public void setIncludedEnvironmentVariables(Map<String, String> environmentVariables) {
        if (includedEnvironmentVariables == null) {
            includedEnvironmentVariables = new TreeMap<>();
        } else {
            includedEnvironmentVariables.clear();
        }

        addIncludedEnvironmentVariables(environmentVariables);
    }

    /**
     * Add environment variables and their associated Splunk field names to the current set of system properties to include in the event.
     *
     * @param environmentVariables a Map of environment variables and their associated Splunk field names.
     */
    public void addIncludedEnvironmentVariables(Map<String, String> environmentVariables) {
        if (includedEnvironmentVariables == null) {
            includedEnvironmentVariables = new TreeMap<>();
        }
        if (environmentVariables != null && !environmentVariables.isEmpty()) {
            environmentVariables.putAll(environmentVariables);
        }
    }

    /**
     * Add environment variables to the current set of environment variables to include in the event.
     *
     * @param environmentVariables a List of environment variables
     */
    public void addIncludedEnvironmentVariables(List<String> environmentVariables) {
        if (includedEnvironmentVariables == null) {
            includedEnvironmentVariables = new TreeMap<>();
        }
        if (environmentVariables != null && !environmentVariables.isEmpty()) {
            for (String environmentVariable : environmentVariables) {
                includeEnvironmentVariable(environmentVariable);
            }
        }
    }

    /**
     * Add an environment variable and it's associated Splunk field name to the current set of environment variables to include in the event.
     *
     * @param environmentVariable an environment variable
     * @param splunkFieldName the Splunk field name for the environment variable
     */
    public void includeEnvironmentVariable(String environmentVariable, String splunkFieldName) {
        if (includedEnvironmentVariables == null) {
            includedEnvironmentVariables = new TreeMap<>();
        }

        if (splunkFieldName != null && !splunkFieldName.isEmpty()) {
            includedEnvironmentVariables.put(environmentVariable, splunkFieldName);
        } else {
            includedEnvironmentVariables.put(environmentVariable, null);
        }
    }

    /**
     * Add an environment variable to the current set of environment variables to include in the event.
     *
     * @param environmentVariable an environment variable
     */
    public void includeEnvironmentVariable(String environmentVariable) {
        if (includedEnvironmentVariables == null) {
            includedEnvironmentVariables = new TreeMap<>();
        }

        includedEnvironmentVariables.put(environmentVariable, null);
    }

    protected boolean checkString(String str) {
        return str != null && !str.isEmpty();
    }

    protected boolean checkMap(Map map) {
        return map != null && !map.isEmpty();
    }

    protected  Map<String, String> createCopyOfMap(Map<String, String> src) {
        Map<String, String> answer = new TreeMap<>();

        if (src != null && !src.isEmpty()) {
            answer.putAll(src);
        }

        return answer;
    }


}

