/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pronoia.splunk.eventcollector.eventbuilder;

import com.pronoia.splunk.eventcollector.EventBuilder;
import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.EventCollectorInfo;
import com.pronoia.splunk.eventcollector.SplunkMDCHelper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class EventBuilderSupport<E> implements EventBuilder<E> {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    String defaultHost;
    String defaultIndex;
    String defaultSource;
    String defaultSourcetype;

    String host;
    String index;
    String source;
    String sourcetype;
    Double timestamp;

    Map<String, String> constantFields;
    Map<String, String> includedSystemProperties;
    Map<String, String> includedEnvironmentVariables;

    Map<String, Object> fields;

    E eventBody;

    public EventBuilderSupport() {
        setDefaultHost();
    }

    @Override
    public boolean hasDefaultHost() {
        return checkString(defaultHost);
    }

    @Override
    public String getDefaultHost() {
        return defaultHost;
    }

    @Override
    public void setDefaultHost(String defaultHost) {
        this.defaultHost = defaultHost;
    }

    @Override
    public boolean hasDefaultIndex() {
        return checkString(defaultIndex);
    }

    @Override
    public String getDefaultIndex() {
        return defaultIndex;
    }

    @Override
    public void setDefaultIndex(String defaultIndex) {
        this.defaultIndex = defaultIndex;
    }

    @Override
    public boolean hasDefaultSource() {
        return checkString(defaultSource);
    }

    @Override
    public String getDefaultSource() {
        return defaultSource;
    }

    @Override
    public void setDefaultSource(String defaultSource) {
        this.defaultSource = defaultSource;
    }

    @Override
    public boolean hasDefaultSourcetype() {
        return checkString(defaultSourcetype);
    }

    @Override
    public String getDefaultSourcetype() {
        return defaultSourcetype;
    }

    @Override
    public void setDefaultSourcetype(String defaultSourcetype) {
        this.defaultSourcetype = defaultSourcetype;
    }

    /**
     * Determine if the instance has a Splunk 'host' default field configured.
     *
     * @return true if 'host' field is configured; false otherwise
     */
    @Override
    public boolean hasHost() {
        return checkString(host);
    }

    /**
     * Get the value of the Splunk 'host' default field for the event that will be used on the next invocation of the
     * build() method.
     *
     * @return the Splunk 'host' default field value
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * Set the value for the Splunk 'host' default field that will be used on the next invocation of the build() method.
     *
     * <p>This value is typically a hostname, IP address or fully qualified domain name of the host from which the event
     * originated.
     *
     * @param host value for the 'host' default field.
     */
    @Override
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Determine if the instance has a Splunk 'index' default field configured.
     *
     * @return true if 'index' field is configured; false otherwise
     */
    @Override
    public boolean hasIndex() {
        return checkString(index);
    }

    /**
     * Get the name of Splunk 'index' default field for the event that will be used on the next invocation of the build()
     * method.
     *
     * @return the Splunk 'index' field value
     */
    @Override
    public String getIndex() {
        return index;
    }

    /**
     * Set the value for the Splunk 'index' default field that will be used on the next invocation of the build() method.
     *
     * <p>NOTE:  The Splunk Index must be created in the Splunk environment prior to use.  Using in invalid value for the
     * Splunk 'index' default field may result in data loss.
     *
     * @param index value of the Splunk Index field
     */
    @Override
    public void setIndex(final String index) {
        this.index = index;
    }


    /**
     * Determine if the instance has a Splunk 'source' default field configured.
     *
     * @return true if 'source' field is configured; false otherwise
     */
    @Override
    public boolean hasSource() {
        return checkString(source);
    }

    /**
     * Get the value of Splunk 'source' default field for the event that will be used on the next invocation of the
     * build() method.
     *
     * @return the Splunk 'source' default field value
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * Set the value for the Splunk 'source' default field that will be used on the next invocation of the build()
     * method.
     *
     * <p>This value is typically the name of the file, stream or other input from which the event originates.
     *
     * @param source value for the 'source' default field.
     */
    @Override
    public void setSource(final String source) {
        this.source = source;
    }

    /**
     * Determine if the instance has a Splunk 'sourcetype' default field configured.
     *
     * @return true if 'sourcetype' field is configured; false otherwise
     */
    @Override
    public boolean hasSourcetype() {
        return checkString(sourcetype);
    }

    /**
     * Get the value of Splunk 'sourcetype' default field for the event that will be used on the next invocation of the
     * build() method.
     *
     * @return the Splunk 'sourcetype' default field value
     */
    @Override
    public String getSourcetype() {
        return sourcetype;
    }

    /**
     * Set the value for the Splunk 'sourcetype' default field that will be used on the next invocation of the build()
     * method.
     *
     * <p>This value is typically a name used to identify the format of the data in the event.
     *
     * @param sourcetype value for the 'sourcetype' default field.
     */
    @Override
    public void setSourcetype(final String sourcetype) {
        this.sourcetype = sourcetype;
    }

    /**
     * Determine if the instance has a Splunk 'timestamp' configured.
     *
     * @return true if 'timestamp' field is configured; false otherwise
     */
    @Override
    public boolean hasTimestamp() {
        return timestamp != null;
    }

    /**
     * Get the value of Splunk 'timestamp' default field for the event that will be used on the next invocation of the
     * build() method.
     *
     * @return the Splunk 'timestamp' default field value
     */
    @Override
    public Double getTimestamp() {
        return timestamp;
    }

    /**
     * Set the value for the Splunk 'timestamp' default field that will be used on the next invocation of the build()
     * method.
     *
     * <p>This value is typically the time at which the event occurred.
     *
     * @param epochSeconds value for the 'timestamp' default field as the number of seconds since 1/1/1970 GMT.
     */
    @Override
    public void setTimestamp(final double epochSeconds) {
        this.timestamp = epochSeconds;
    }

    /**
     * Determine if the instance has constant Splunk fields configured.
     *
     * @return true if constant Splunk fields are configured; false otherwise
     */
    @Override
    public boolean hasConstantFields() {
        return checkMap(constantFields);
    }

    /**
     * Get a copy of the constant Splunk fields and values that will be included in the event.
     *
     * @return a copy of the included system properties Map
     */
    @Override
    public Map<String, String> getConstantFields() {
        return getConstantFields(true);
    }

    /**
     * Get a copy of the constant Splunk fields and values that will be included in the event.
     *
     * @return a copy of the included system properties Map
     */
    @Override
    public Map<String, String> getConstantFields(boolean copy) {
        return copy ? createCopyOfMap(constantFields) : constantFields;
    }

    @Override
    public boolean hasIncludedSystemProperties() {
        return checkMap(includedSystemProperties);
    }

    /**
     * Get a copy of the system properties and their associated Splunk field names that will be included in the event.
     *
     * @return a copy of the included system properties Map
     */
    @Override
    public Map<String, String> getIncludedSystemProperties() {
        return getIncludedSystemProperties(true);
    }

    /**
     * Get a copy of the system properties and their associated Splunk field names that will be included in the event.
     *
     * @return a copy of the included system properties Map
     */
    @Override
    public Map<String, String> getIncludedSystemProperties(boolean copy) {
        return copy ? createCopyOfMap(includedSystemProperties) : includedSystemProperties;
    }

    @Override
    public boolean hasIncludedEnvironmentVariables() {
        return checkMap(includedEnvironmentVariables);
    }

    /**
     * Get a copy of the environment variables and their associated Splunk field names that will be included in the event.
     *
     * @return a copy of the included environment variable Map
     */
    @Override
    public Map<String, String> getIncludedEnvironmentVariables() {
        return getIncludedEnvironmentVariables(true);
    }

    /**
     * Get the environment variables and their associated Splunk field names that will be included in the event, optionally copying the Map.
     *
     * @return a copy of the included environment variable Map
     */
    @Override
    public Map<String, String> getIncludedEnvironmentVariables(boolean copy) {
        return copy ? createCopyOfMap(includedEnvironmentVariables) : includedEnvironmentVariables;
    }

    /**
     * Determine if the instance has an event configured.
     *
     * @return true if an event has been configured; false otherwise
     */
    @Override
    public boolean hasEventBody() {
        return eventBody != null;
    }

    /**
     * Get the body of the event that will be used on the next invocation of the build() method.
     *
     * @return the event body
     */
    @Override
    public E getEventBody() {
        return eventBody;
    }

    /**
     * Set the body of the event (the _raw field) that will be used on the next invocation of the build() method.
     *
     * @param eventBody the body of the event
     */
    @Override
    public void setEventBody(final E eventBody) {
        this.eventBody = eventBody;
    }

    /**
     * Determine if the instance has additional Splunk fields configured.
     *
     * @return true if additional Splunk fields are configured; false otherwise
     */
    @Override
    public boolean hasFields() {
        return checkMap(fields);
    }

    /**
     * Get the map of additional field names and values that will be used on the next invocation of the build() method.
     *
     * @return the map of field names and values
     */
    @Override
    public Map<String, Object> getFields() {
        if (fields == null) {
            fields = new LinkedHashMap<>();
        }

        return fields;
    }

    /**
     * Set the map of additional field names and values that will be used on the next invocation of the build() method.
     *
     * <p>NOTE:  Additional indexed fields are only supported on Splunk 6.5 or greater, and INDEXED_EXTRACTION must be set
     * to JSON for the sourcetype in props.conf.
     *
     * @param fieldMap the map of field names and values
     */
    @Override
    public void setFields(final Map<String, Object> fieldMap) {
        clearFields();

        addFields(fieldMap);
    }

    /**
     * Set the value of a indexed field that will be used on the next invocation of the build() method.
     *
     * <p>NOTE:  Additional indexed Fields are only supported on Splunk 6.5 or greater, and INDEXED_EXTRACTION must be set
     * to JSON for the sourcetype in props.conf.
     *
     * @param fieldName   the name of the indexed field.
     * @param fieldValues the value(s) of the indexed field
     */
    @Override
    public void setField(final String fieldName, final String... fieldValues) {
        getFields();

        try (SplunkMDCHelper helper = createMdcHelper()) {
            if (fieldName == null) {
                log.warn("Null field name - ignoring value(s): {}", fieldValues);
            } else if (fieldName.isEmpty()) {
                log.warn("Empty field name - ignoring value(s): {}", fieldValues);
            } else {
                if (fieldValues == null) {
                    fields.remove(fieldName);
                } else {
                    switch (fieldValues.length) {
                    case 0:
                        // Remove the empty field
                        fields.remove(fieldName);
                        break;
                    case 1:
                        if (fieldValues[0] == null || fieldValues[0].isEmpty()) {
                            fields.remove(fieldName);
                        } else {
                            // Add a single String
                            fields.put(fieldName, fieldValues[0]);
                        }
                        break;
                    default:
                        // Add all values
                        fields.put(fieldName, Arrays.asList(fieldValues));
                    }
                }
            }
        }
    }

    /**
     * Add the map of additional field names and values that will be used on the next invocation of the build() method.
     *
     * <p>NOTE:  Additional indexed fields are only supported on Splunk 6.5 or greater, and INDEXED_EXTRACTION must be set
     * to JSON for the sourcetype in props.conf.
     *
     * @param fieldMap the map of field names and values
     */
    @Override
    public void addFields(final Map<String, Object> fieldMap) {
        if (fieldMap != null && !fieldMap.isEmpty()) {
            Map<String, Object> currentFields = getFields();
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                Object entryValue = entry.getValue();
                if (entryValue != null) {
                    currentFields.put(entry.getKey(), entryValue);
                }
            }
        }
    }

    /**
     * ADD the value of a indexed field that will be used on the next invocation of the build() method.
     *
     * <p>NOTE:  Additional indexed Fields are only supported on Splunk 6.5 or greater, and INDEXED_EXTRACTION must be set
     * to JSON for the sourcetype in props.conf.
     *
     * @param fieldName   the name of the indexed field.
     * @param fieldValues the value(s) of the indexed field
     */
    @Override
    public void addField(final String fieldName, final String... fieldValues) {
        setField(fieldName, fieldValues);
    }

    /**
     * Clear the map of additional fields that will be used on the next invocation of the build() method using a fluent
     * builder.
     *
     * @return the current EventBuilder
     */
    @Override
    public EventBuilderSupport<E> clearFields() {
        if (this.fields != null) {
            this.fields.clear();
        }
        return this;
    }

    /**
     * Build the JSON-formatted event suitable for the Splunk HTTP Event Collector.
     *
     * @return the JSON-formatted event
     */
    @Override
    public String build(EventCollectorClient client) {
        Map<String, Object> eventMap = new LinkedHashMap<>();

        addDefaultFieldsToMap(client, eventMap);

        Map<String, Object> additionalFields = new LinkedHashMap<>();
        addAdditionalFieldsToMap(client, additionalFields);
        if (!additionalFields.isEmpty()) {
            eventMap.put(EventCollectorInfo.FIELDS_KEY, additionalFields);
        }

        addEventBodyToMap(eventMap);

        String answer = convertMapToJson(eventMap);

        resetTransientData();

        return answer;
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

    protected abstract String convertMapToJson(Map<String, Object> map);

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
     * @param splunkFieldValue the value for the field
     */
    public void addConstantField(String splunkFieldName, String splunkFieldValue) {
        if (this.constantFields == null) {
            this.constantFields = new TreeMap<>();
        }

        this.constantFields.put(splunkFieldName, splunkFieldValue);
    }

    /**
     * Remove a constant Splunk field from the current set of constant Splunk fields to include in the event.
     *
     * @param splunkFieldName the Splunk field name
     */
    public void removeConstantField(String splunkFieldName) {
        if (this.constantFields != null) {
            this.constantFields.remove(splunkFieldName);
        }
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
     * @param systemProperty  a system property key
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
     * Remove a system property from the current set of system properties to include in the event.
     *
     * @param systemProperty a system property key
     */
    public void removeSystemProperty(String systemProperty) {
        if (includedSystemProperties != null) {
            includedSystemProperties.remove(systemProperty);
        }
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
            includedEnvironmentVariables.putAll(environmentVariables);
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
     * @param splunkFieldName     the Splunk field name for the environment variable
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

    /**
     * Determine the value for the Splunk host field.
     *
     * @param client the event collector client
     *
     * @return the value for the Splunk host field, or null if the Splunk host field should NOT be included in the event.
     */
    public String getHostFieldValue(EventCollectorClient client) {
        String answer = null;

        if (hasHost()) {
            answer = host;
        } else if (hasDefaultHost()) {
            answer = defaultHost;
        } else if (client.hasEventHost()) {
            answer = client.getEventHost();
        }

        return answer;
    }

    /**
     * Determine the value for the Splunk index field.
     *
     * @param client the event collector client
     *
     * @return the value for the Splunk index field, or null if Splunk index field should NOT be included in the event.
     */
    public String getIndexFieldValue(EventCollectorClient client) {
        String answer = null;

        if (hasIndex()) {
            answer = index;
        } else if (hasDefaultIndex()) {
            answer = defaultIndex;
        } else if (client.hasEventIndex()) {
            answer = client.getEventIndex();
        }

        return answer;
    }

    /**
     * Determine the value for the Splunk source field.
     *
     * @param client the event collector client
     *
     * @return the value for the Splunk source field, or null if Splunk source field should NOT be included in the event.
     */
    public String getSourceFieldValue(EventCollectorClient client) {
        String answer = null;

        if (hasSource()) {
            answer = source;
        } else if (hasDefaultSource()) {
            answer = defaultSource;
        } else if (client.hasEventSource()) {
            answer = client.getEventSource();
        }

        return answer;
    }

    /**
     * Determine the value for the Splunk sourcetype field.
     *
     * @param client the event collector client
     *
     * @return the value for the Splunk sourcetype field, or null if Splunk sourcetype field should NOT be included in the event.
     */
    public String getSourcetypeFieldValue(EventCollectorClient client) {
        String answer = null;

        if (hasSourcetype()) {
            answer = sourcetype;
        } else if (hasDefaultSourcetype()) {
            answer = defaultSourcetype;
        } else if (client.hasEventSourcetype()) {
            answer = client.getEventSourcetype();
        }

        return answer;
    }

    /**
     * Determine the value for the Splunk timestamp field.
     *
     * @return the value for the Splunk timestamp field.
     */
    public String getTimestampFieldValue() {
        final String timestampFormat = "%.3f";
        String answer = null;

        if (hasTimestamp()) {
            answer = String.format(String.format(timestampFormat, timestamp));
        } else {
            answer = String.format(String.format(timestampFormat, System.currentTimeMillis() / 1000.0));
        }

        return answer;
    }

    /**
     * Add the default Splunk fields to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param client the event collector client
     * @param map the target Map for the field values
     */
    protected void addDefaultFieldsToMap(EventCollectorClient client, Map<String, Object> map) {
        try (SplunkMDCHelper helper = createMdcHelper()) {

            log.debug("Adding default event fields");

            String hostFieldValue = getHostFieldValue(client);
            if (hostFieldValue != null && !hostFieldValue.isEmpty()) {
                log.debug("Adding '{}'={}", EventCollectorInfo.HOST_KEY, hostFieldValue);
                map.put(EventCollectorInfo.HOST_KEY, hostFieldValue);
            }

            String indexFieldValue = getIndexFieldValue(client);
            if (indexFieldValue != null && !indexFieldValue.isEmpty()) {
                log.debug("Adding '{}'={}", EventCollectorInfo.INDEX_KEY, indexFieldValue);
                map.put(EventCollectorInfo.INDEX_KEY, indexFieldValue);
            }

            String sourceFieldValue = getSourceFieldValue(client);
            if (sourceFieldValue != null && !sourceFieldValue.isEmpty()) {
                log.debug("Adding '{}'={}", EventCollectorInfo.SOURCE_KEY, sourceFieldValue);
                map.put(EventCollectorInfo.SOURCE_KEY, sourceFieldValue);
            }

            String sourcetypeFieldValue = getSourcetypeFieldValue(client);
            if (sourcetypeFieldValue != null && !sourcetypeFieldValue.isEmpty()) {
                log.debug("Adding '{}'={}", EventCollectorInfo.SOURCETYPE_KEY, sourcetypeFieldValue);
                map.put(EventCollectorInfo.SOURCETYPE_KEY, sourcetypeFieldValue);
            }

            String timestampFieldValue = getTimestampFieldValue();
            if (timestampFieldValue != null && !timestampFieldValue.isEmpty()) {
                log.debug("Adding '{}'={}", EventCollectorInfo.TIMESTAMP_KEY, timestampFieldValue);
                map.put(EventCollectorInfo.TIMESTAMP_KEY, timestampFieldValue);
            }
        }
    }

    /**
     * Add the additional Splunk fields to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param client the event collector client
     * @param map the target Map for the field values
     */
    protected void addAdditionalFieldsToMap(EventCollectorClient client, Map<String, Object> map) {
        try (SplunkMDCHelper helper = createMdcHelper()) {
            log.debug("Adding additional event fields");

            if (client.hasIncludedEnvironmentVariables()) {
                addEnvironmentVariablesToMap(client, map);
            }

            if (hasIncludedEnvironmentVariables()) {
                addEnvironmentVariablesToMap(map);
            }

            if (client.hasIncludedSystemProperties()) {
                addSystemPropertiesToMap(client, map);
            }

            if (hasIncludedSystemProperties()) {
                addSystemPropertiesToMap(map);
            }

            if (hasFields()) {
                map.putAll(fields);
            }

            if (client.hasConstantFields()) {
                addConstantFieldsToMap(client, map);
            }

            if (hasConstantFields()) {
                addConstantFieldsToMap(map);
            }
        }
    }

    /**
     * Add the constant Splunk fields to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param client the Splunk client with constant fields
     * @param map    the target Map for the constant field values
     */
    protected void addConstantFieldsToMap(EventCollectorClient client, Map<String, Object> map) {
        if (map != null && client != null && client.hasConstantFields()) {
            try (SplunkMDCHelper helper = createMdcHelper()) {
                log.debug("Adding constant Splunk fields from event collector client to event");

                doAddConstantFieldsToMap(client.getConstantFields(false), map);
            }
        }
    }

    /**
     * Add the constant Splunk fields to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param map the target Map for the constant field values
     */
    protected void addConstantFieldsToMap(Map<String, Object> map) {
        if (map != null && hasConstantFields()) {
            try (SplunkMDCHelper helper = createMdcHelper()) {
                log.debug("Adding constant Splunk fields from event builder to event");

                doAddConstantFieldsToMap(constantFields, map);
            }
        }
    }

    /**
     * Add the constant Splunk fields to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param constantFieldsSource the source map of constand fields and their values
     * @param target               the target Map for the system property values
     */
    protected void doAddConstantFieldsToMap(Map<String, String> constantFieldsSource, Map<String, Object> target) {
        if (target != null && constantFieldsSource != null && !constantFieldsSource.isEmpty()) {
            for (Map.Entry<String, String> constantField : constantFieldsSource.entrySet()) {
                String splunkFieldName = constantField.getKey();
                String splunkFieldValue = constantField.getValue();
                if (splunkFieldName != null && splunkFieldValue != null && !splunkFieldName.isEmpty() && !splunkFieldValue.isEmpty()) {
                    target.put(splunkFieldName, splunkFieldValue);
                }
            }
        }
    }

    /**
     * Add the system properties to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param client the Splunk client with system properties
     * @param map    the target Map for the system property values
     */
    protected void addSystemPropertiesToMap(EventCollectorClient client, Map<String, Object> map) {
        if (map != null && client.hasIncludedSystemProperties()) {
            try (SplunkMDCHelper helper = createMdcHelper()) {
                log.debug("Adding system properties from the event collector client to the event");

                doAddSystemPropertiesToMap(client.getIncludedSystemProperties(false), map);
            }
        }
    }

    /**
     * Add the system properties to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param map the target Map for the system property values
     */
    protected void addSystemPropertiesToMap(Map<String, Object> map) {
        if (map != null && hasIncludedSystemProperties()) {
            try (SplunkMDCHelper helper = createMdcHelper()) {
                log.debug("Adding system properties from the event builder to the event");
                doAddSystemPropertiesToMap(includedSystemProperties, map);
            }
        }
    }

    /**
     * Add the system properties to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param systemPropertiesSource the source map of system properties and their associated field names
     * @param target                 the target Map for the system property values
     */
    protected void doAddSystemPropertiesToMap(Map<String, String> systemPropertiesSource, Map<String, Object> target) {
        if (target != null && systemPropertiesSource != null && !systemPropertiesSource.isEmpty()) {
            for (Map.Entry<String, String> systemPropertyEntry : systemPropertiesSource.entrySet()) {
                String systemPropertyName = systemPropertyEntry.getKey();
                if (systemPropertyName != null || !systemPropertyName.isEmpty()) {
                    String systemPropertyValue = System.getProperty(systemPropertyName, null);
                    if (systemPropertyValue != null && !systemPropertyValue.isEmpty()) {
                        String splunkFieldName = systemPropertyEntry.getValue();
                        if (splunkFieldName == null || splunkFieldName.isEmpty()) {
                            splunkFieldName = systemPropertyName;
                            systemPropertyEntry.setValue(systemPropertyName);
                        }
                        target.put(splunkFieldName, systemPropertyValue);
                    } else {
                        log.warn("System property {} not found - ignoring", systemPropertyName);
                    }
                }
            }
        }
    }

    /**
     * Add the environment variables to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param client the Splunk client with environment variables
     * @param map    the target Map for the environment variable values
     */
    protected void addEnvironmentVariablesToMap(EventCollectorClient client, Map<String, Object> map) {
        if (map != null && client.hasIncludedEnvironmentVariables()) {
            try (SplunkMDCHelper helper = createMdcHelper()) {
                log.debug("Adding environment variables from the event collector client to the event");
                doAddEnvironmentVariablesToMap(client.getIncludedEnvironmentVariables(false), map);
            }
        }
    }

    /**
     * Add the environment variables to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param map the target Map for the environment variable values
     */
    protected void addEnvironmentVariablesToMap(Map<String, Object> map) {
        if (map != null && hasIncludedEnvironmentVariables()) {
            try (SplunkMDCHelper helper = createMdcHelper()) {
                log.debug("Adding environment variables from the event builder to the event");
                doAddEnvironmentVariablesToMap(includedEnvironmentVariables, map);
            }
        }
    }

    /**
     * Add the environment variables to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param environmentVariblesSource the source map of environment variables and their associated field names
     * @param target                    the target Map for the environment variable values
     */
    protected void doAddEnvironmentVariablesToMap(Map<String, String> environmentVariblesSource, Map<String, Object> target) {
        try (SplunkMDCHelper helper = createMdcHelper()) {
            log.debug("Adding system properties to the event");

            if (target != null && environmentVariblesSource != null && !environmentVariblesSource.isEmpty()) {
                for (Map.Entry<String, String> environmentVariableEntry : environmentVariblesSource.entrySet()) {
                    String environmentVariableName = environmentVariableEntry.getKey();
                    if (environmentVariableName != null || !environmentVariableName.isEmpty()) {
                        String environmentVariableValue = System.getenv(environmentVariableName);
                        if (environmentVariableValue != null && !environmentVariableValue.isEmpty()) {
                            String splunkFieldName = environmentVariableEntry.getValue();
                            if (splunkFieldName == null || splunkFieldName.isEmpty()) {
                                splunkFieldName = environmentVariableName;
                                environmentVariableEntry.setValue(environmentVariableName);
                            }
                            target.put(splunkFieldName, environmentVariableValue);
                        } else {
                            log.warn("Environment variable {} not found - ignoring", environmentVariableName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Add the body to the event.
     *
     * <p>This method is called by the 'build' method.
     *
     * @param map the target eventObject Map&lt;String,Object&gt; for the body
     */
    protected void addEventBodyToMap(Map<String, Object> map) {
        try (SplunkMDCHelper helper = createMdcHelper()) {
            log.debug("Adding event body");
            if (hasEventBody()) {
                if (eventBody instanceof Map || eventBody instanceof List) {
                    map.put(EventCollectorInfo.EVENT_BODY_KEY, eventBody);
                } else {
                    map.put(EventCollectorInfo.EVENT_BODY_KEY, eventBody.toString());
                }
            } else {
                map.put(EventCollectorInfo.EVENT_BODY_KEY, "null event body");
            }
        }
    }

    /**
     * Utility method for adding values to a eventObject Map.
     *
     * <p>If the supplied key or value is null, the value is not added to the eventObject Map.
     *
     * @param eventObject the target map for the values
     * @param key         the 'key' of the value
     * @param value       the value itself
     */
    protected void putIfValueIsNotNull(Map<String, Object> eventObject, final String key, final String value) {
        try (SplunkMDCHelper helper = createMdcHelper()) {
            if (eventObject == null) {
                log.error("Null eventObject Map - ignoring {} = {}", key, value);
            } else if (key == null) {
                log.warn("Null key - ignoring value: {}", value);
            } else if (key.isEmpty()) {
                log.warn("Empty key - ignoring value: {}", value);
            } else if (value != null) {
                eventObject.put(key, value);
            }
        }
    }

    protected void resetTransientData() {
        this.host = null;
        this.index = null;
        this.source = null;
        this.sourcetype = null;
        this.timestamp = null;
        this.eventBody = null;

        if (hasFields()) {
            clearFields();
        }
    }

    protected void copyConfiguration(EventBuilderSupport<E> sourceEventBuilder) {
        try (SplunkMDCHelper helper = createMdcHelper()) {
            log.debug("Copying EventBuilder configuration ; source-event-builder = {}, target-event-builder = {}", sourceEventBuilder, this);
            this.defaultHost = sourceEventBuilder.defaultHost;
            this.defaultIndex = sourceEventBuilder.defaultIndex;
            this.defaultSource = sourceEventBuilder.defaultSource;
            this.defaultSourcetype = sourceEventBuilder.defaultSourcetype;

            this.setConstantFields(sourceEventBuilder.constantFields);
            this.setIncludedSystemProperties(sourceEventBuilder.includedSystemProperties);
            this.setIncludedEnvironmentVariables(sourceEventBuilder.includedEnvironmentVariables);
        }
    }

    protected void appendConfiguration(StringBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("appendConfiguration(StringBuilder) - StringBuilder argument cannot be null");
        }

        if (hasDefaultHost()) {
            builder.append(" defaultHost='").append(defaultHost).append('\'');
        }
        if (hasDefaultIndex()) {
            builder.append(" defaultIndex='").append(defaultIndex).append('\'');
        }
        if (hasDefaultSource()) {
            builder.append(" defaultSource='").append(defaultSource).append('\'');
        }
        if (hasDefaultSourcetype()) {
            builder.append(" defaultSourcetype='").append(defaultSourcetype).append('\'');
        }
        if (hasHost()) {
            builder.append(" host='").append(host).append('\'');
        }
        if (hasIndex()) {
            builder.append(" index='").append(index).append('\'');
        }
        if (hasSource()) {
            builder.append(" source='").append(source).append('\'');
        }
        if (hasSourcetype()) {
            builder.append(" sourcetype='").append(sourcetype).append('\'');
        }
        if (hasTimestamp()) {
            builder.append(" timestamp='").append(timestamp).append('\'');
        }
        if (hasIncludedSystemProperties()) {
            builder.append(" includedSystemProperties='").append(includedSystemProperties).append('\'');
        }
        if (hasConstantFields()) {
            builder.append(" constantFields='").append(constantFields).append('\'');
        }

        return;
    }


    protected SplunkMDCHelper createMdcHelper() {
        return new EventBuilderMDCHelper(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.getClass().getSimpleName())
            .append('{');

        this.appendConfiguration(builder);

        builder.append('}');

        return builder.toString();
    }

    protected boolean checkString(String str) {
        return str != null && !str.isEmpty();
    }


    protected boolean checkMap(Map map) {
        return map != null && !map.isEmpty();
    }

    protected Map<String, String> createCopyOfMap(Map<String, String> src) {
        Map<String, String> answer = new TreeMap<>();

        if (src != null && !src.isEmpty()) {
            answer.putAll(src);
        }

        return answer;
    }

}
