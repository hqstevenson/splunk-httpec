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

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.EventCollectorInfo;
import com.pronoia.splunk.eventcollector.EventDeliveryException;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Client for sending JSON-formatted events to a multiple Splunk HTTP Collectors.
 *
 * <p>TODO:  Implement this class
 */
public class LoadBalancingEventCollectorClient implements EventCollectorClient {
    Logger log = LoggerFactory.getLogger(this.getClass());
    List<EventCollectorInfo> eventCollectorInfoSet = new LinkedList<>();
    Deque<HttpClient> httpClients;

    public LoadBalancingEventCollectorClient() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public String getClientId() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasEventHost() {
        return false;
    }

    @Override
    public String getEventHost() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasEventIndex() {
        return false;
    }

    @Override
    public String getEventIndex() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasEventSource() {
        return false;
    }

    @Override
    public String getEventSource() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasEventSourcetype() {
        return false;
    }

    @Override
    public String getEventSourcetype() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasConstantFields() {
        return false;
    }

    @Override
    public Map<String, String> getConstantFields() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public Map<String, String> getConstantFields(boolean copy) {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasIncludedSystemProperties() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public Map<String, String> getIncludedSystemProperties() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public Map<String, String> getIncludedSystemProperties(boolean copy) {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public boolean hasIncludedEnvironmentVariables() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public Map<String, String> getIncludedEnvironmentVariables() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public Map<String, String> getIncludedEnvironmentVariables(boolean copy) {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public synchronized void start() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public synchronized void stop() {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }

    @Override
    public void sendEvent(String event) throws EventDeliveryException {
        throw new UnsupportedOperationException("This class has not yet been implemented");
    }
}
