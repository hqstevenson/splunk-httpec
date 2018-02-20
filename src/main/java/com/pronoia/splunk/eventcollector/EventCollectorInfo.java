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
package com.pronoia.splunk.eventcollector;

/**
 * Splunk HTTP Event Collector Information.
 */
public final class EventCollectorInfo {
    public static final String INDEX_KEY = "index";
    public static final String HOST_KEY = "host";
    public static final String SOURCE_KEY = "source";
    public static final String SOURCETYPE_KEY = "sourcetype";
    public static final String FIELDS_KEY = "fields";
    public static final String TIMESTAMP_KEY = "time";
    public static final String EVENT_BODY_KEY = "event";

    String host;
    Integer port;
    String authorizationToken;
    boolean validateCertificates = true;

    String cachedPostUrl;
    String cachedAuthorizationHeaderValue;

    public EventCollectorInfo() {
    }

    /**
     * Get the hostname or IP address of the Splunk HTTP Event Collector.
     *
     * @return hostname or IP
     */
    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
        this.cachedPostUrl = null;
    }

    /**
     * Get the port of the Splunk HTTP Event Collector.
     *
     * @return port
     */
    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
        this.cachedPostUrl = null;
    }

    /**
     * Get the authorization token for Splunk HTTP Event Collector.
     *
     * @return port
     */
    public String getAuthorizationToken() {
        return authorizationToken;
    }

    /**
     * Set the Splunk HTTP Event Collector Authorization Token.
     *
     * @param authorizationToken the authorization token
     */
    public void setAuthorizationToken(final String authorizationToken) {
        this.authorizationToken = authorizationToken;
        this.cachedAuthorizationHeaderValue = null;
    }

    /**
     * Determine if SSL certificate validation is enabled.
     *
     * @return true if certificate validation is enabled; false otherwise
     */
    public boolean isCertificateValidationEnabled() {
        return validateCertificates;
    }

    /**
     * Enable or disable SSL certificate validation.
     *
     * @param validateCertificates true enables certificate validation; false disables certificate
     *                             validation
     */
    public void setValidateCertificates(final boolean validateCertificates) {
        this.validateCertificates = validateCertificates;
    }

    /**
     * Enable SSL certificate validation.
     */
    public void enableCertificateValidation() {
        this.validateCertificates = true;
    }

    /**
     * Disable SSL certificate validation.
     */
    public void disableCertificateValidation() {
        this.validateCertificates = false;
    }

    /**
     * Get the URL Splunk HTTP Event Collector.
     *
     * @return HTTP POST URL as a String
     */
    public String getPostUrl() {
        if (cachedPostUrl == null) {
            cachedPostUrl = String.format("https://%s:%d/services/collector", getHost(), getPort());
        }

        return cachedPostUrl;
    }

    /**
     * Get the value that will be used for the HTTP Authorization header.
     *
     * @return HTTP Authorization header value
     */
    public String getAuthorizationHeaderValue() {
        if (cachedAuthorizationHeaderValue == null) {
            cachedAuthorizationHeaderValue = String.format("Splunk %s", getAuthorizationToken());
        }
        return cachedAuthorizationHeaderValue;
    }

}
