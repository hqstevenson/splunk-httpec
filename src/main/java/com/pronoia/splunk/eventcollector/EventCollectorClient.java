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

import java.util.Map;


/**
 * Client Interface for sending JSON-formatted events to the Splunk HTTP
 * Collector.
 */
public interface EventCollectorClient {

    String getClientId();

    boolean hasEventHost();

    String getEventHost();

    boolean hasEventIndex();

    String getEventIndex();

    boolean hasEventSource();

    String getEventSource();

    boolean hasEventSourcetype();

    String getEventSourcetype();

    boolean hasConstantFields();
    Map<String, String> getConstantFields();
    Map<String, String> getConstantFields(boolean copy);

    boolean hasIncludedSystemProperties();
    Map<String, String> getIncludedSystemProperties();
    Map<String, String> getIncludedSystemProperties(boolean copy);

    boolean hasIncludedEnvironmentVariables();
    Map<String, String> getIncludedEnvironmentVariables();
    Map<String, String> getIncludedEnvironmentVariables(boolean copy);

    /**
     * Initialize the client.
     *
     * <p>If the client is already initialized, the attempt to initialize a
     * previously initialized client will be logged and the call will be ignored.
     */
    void start();


    /**
     * Cleanup the client.
     *
     * <p>If the client is not initialized, the attempt to destroy an
     * uninitialized client will be logged and the call will be ignored.
     */
    void stop();


    /**
     * Send an event to Splunk.
     *
     * <p>The payload must be property formatted for the Splunk HTTP Event
     * Collector.
     *
     * <p>NOTE:  If the client has not yet been initialized, the client will be
     * initialized.
     *
     * @param event JSON-formatted Event
     *
     * @throws EventDeliveryException in the event the Splunk event could not be delivered to the
     *                                indexer.
     */
    void sendEvent(String event) throws EventDeliveryException;
}
