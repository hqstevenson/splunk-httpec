package com.pronoia.splunk.eventcollector.stub;

import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.EventDeliveryException;

import java.util.Map;
import java.util.TreeMap;


public class EventCollectorClientStub implements EventCollectorClient {
    public Map<String, String> constantFields = new TreeMap<>();
    public Map<String, String> includedSystemProperties = new TreeMap<>();
    public Map<String, String> includedEnvironmentVariables = new TreeMap<>();

    @Override
    public String getClientId() {
        return "stubbed-client";
    }

    @Override
    public boolean hasEventHost() {
        return false;
    }

    @Override
    public String getEventHost() {
        return null;
    }

    @Override
    public boolean hasEventIndex() {
        return false;
    }

    @Override
    public String getEventIndex() {
        return null;
    }

    @Override
    public boolean hasEventSource() {
        return false;
    }

    @Override
    public String getEventSource() {
        return null;
    }

    @Override
    public boolean hasEventSourcetype() {
        return false;
    }

    @Override
    public String getEventSourcetype() {
        return null;
    }

    @Override
    public boolean hasConstantFields() {
        return constantFields != null && !constantFields.isEmpty();
    }

    @Override
    public Map<String, String> getConstantFields() {
        return null;
    }

    @Override
    public Map<String, String> getConstantFields(boolean copy) {
        return null;
    }

    @Override
    public boolean hasIncludedSystemProperties() {
        return includedSystemProperties != null && !includedSystemProperties.isEmpty();
    }

    @Override
    public Map<String, String> getIncludedSystemProperties() {
        return null;
    }

    @Override
    public Map<String, String> getIncludedSystemProperties(boolean copy) {
        return null;
    }

    @Override
    public boolean hasIncludedEnvironmentVariables() {
        return includedEnvironmentVariables != null && !includedEnvironmentVariables.isEmpty();
    }

    @Override
    public Map<String, String> getIncludedEnvironmentVariables() {
        return null;
    }

    @Override
    public Map<String, String> getIncludedEnvironmentVariables(boolean copy) {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void sendEvent(String event) throws EventDeliveryException {

    }

    Map<String, String> createCopy(Map<String, String> src) {
        Map<String, String> answer = new TreeMap<>();

        if (src != null && !src.isEmpty()) {
            answer.putAll(src);
        }

        return answer;
    }
}
