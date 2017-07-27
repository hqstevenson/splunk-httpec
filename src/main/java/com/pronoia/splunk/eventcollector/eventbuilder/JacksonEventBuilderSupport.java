/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.pronoia.splunk.eventcollector.eventbuilder;

import static com.pronoia.splunk.eventcollector.EventCollectorInfo.EVENT_BODY_KEY;
import static com.pronoia.splunk.eventcollector.EventCollectorInfo.FIELDS_KEY;
import static com.pronoia.splunk.eventcollector.EventCollectorInfo.HOST_KEY;
import static com.pronoia.splunk.eventcollector.EventCollectorInfo.INDEX_KEY;
import static com.pronoia.splunk.eventcollector.EventCollectorInfo.SOURCETYPE_KEY;
import static com.pronoia.splunk.eventcollector.EventCollectorInfo.SOURCE_KEY;
import static com.pronoia.splunk.eventcollector.EventCollectorInfo.TIMESTAMP_KEY;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pronoia.splunk.eventcollector.EventBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class JacksonEventBuilderSupport<E> implements EventBuilder<E> {

  protected Logger log = LoggerFactory.getLogger(this.getClass());

  Double timestamp;
  String host;
  String source;
  String sourcetype;
  String index;

  Map<String, String> includedSystemProperties;

  Map<String, Object> fields;

  E eventBody;

  /**
   * Determine if the instance has a Splunk 'index' default field configured.
   *
   * @return true if 'index' field is configured; false otherwise
   */
  @Override
  public boolean hasIndex() {
    return host != null && !host.isEmpty();
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
   * Determine if the instance has a Splunk 'host' default field configured.
   *
   * @return true if 'host' field is configured; false otherwise
   */
  @Override
  public boolean hasHost() {
    return index != null && !index.isEmpty();
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
   * Determine if the instance has a Splunk 'source' default field configured.
   *
   * @return true if 'source' field is configured; false otherwise
   */
  @Override
  public boolean hasSource() {
    return source != null && !source.isEmpty();
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
    return sourcetype != null && !sourcetype.isEmpty();
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
   * Determine if the instance has additional Splunk fields configured.
   *
   * @return true if additional Splunk fields are configured; false otherwise
   */
  @Override
  public boolean hasFields() {
    return fields != null && !fields.isEmpty();
  }

  /**
   * Get the map of additional field names and values that will be used on the next invocation of the build() method.
   *
   * @return the map of field names and values
   */
  @Override
  public Map<String, Object> getFields() {
    if (fields == null) {
      fields = new HashMap<>();
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
    getFields();

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

  /**
   * Clear the map of additional fields that will be used on the next invocation of the build() method using a fluent
   * builder.
   *
   * @return the current EventBuilder
   */
  @Override
  public JacksonEventBuilderSupport<E> clearFields() {
    if (this.fields != null) {
      this.fields.clear();
    }
    return this;
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
   * Determine if the instance has an event configured.
   *
   * @return true if an event has been configured; false otherwise
   */
  @Override
  public boolean hasEvent() {
    return eventBody != null;
  }

  /**
   * Get the body of the event that will be used on the next invocation of the build() method.
   *
   * @return the event body
   */
  @Override
  public E getEvent() {
    return eventBody;
  }

  /**
   * Set the body of the event (the _raw field) that will be used on the next invocation of the build() method.
   *
   * @param eventBody the body of the event
   */
  @Override
  public void setEvent(final E eventBody) {
    this.eventBody = eventBody;
  }

  /**
   * Build the JSON-formatted event suitable for the Splunk HTTP Event Collector.
   *
   * @return the JSON-formatted event
   */
  @Override
  public String build() {
    Map<String, Object> mapObject = new HashMap<>();
    serializeFields(mapObject);
    serializeBody(mapObject);
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = "{}";
    try {
      jsonString = objectMapper.writeValueAsString(mapObject);
      log.debug("JSON:{}", jsonString);
    } catch (Exception e) {
      log.error("FATAL! Can't write JSON String", e);
    }
    return jsonString;
  }

  public boolean hasIncludedSystemProperties() {
    return includedSystemProperties != null && !includedSystemProperties.isEmpty();
  }

  public Map<String, String> getIncludedSystemProperties() {
    Map<String, String> answer = new TreeMap<>();

    if (hasIncludedSystemProperties()) {
      answer.putAll(includedSystemProperties);
    }

    return answer;
  }

  public void setIncludedSystemProperties(List<String> systemProperties) {
    if (includedSystemProperties == null) {
      includedSystemProperties = new TreeMap<>();
    } else {
      includedSystemProperties.clear();
    }

    addIncludedSystemProperties(systemProperties);
  }

  public void setIncludedSystemProperties(Map<String, String> systemProperties) {
    if (includedSystemProperties == null) {
      includedSystemProperties = new TreeMap<>();
    } else {
      includedSystemProperties.clear();
    }

    addIncludedSystemProperties(systemProperties);
  }

  public void addIncludedSystemProperties(Map<String, String> systemProperties) {
    if (includedSystemProperties == null) {
      includedSystemProperties = new TreeMap<>();
    }
    if (systemProperties != null && !systemProperties.isEmpty()) {
      includedSystemProperties.putAll(systemProperties);
    }
  }

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

  public void includeSystemProperty(String systemProperty, String splunkFieldName) {
    if (includedSystemProperties == null) {
      includedSystemProperties = new TreeMap<>();
    }

    includedSystemProperties.put(systemProperty, splunkFieldName);
  }

  public void includeSystemProperty(String systemProperty) {
    if (includedSystemProperties == null) {
      includedSystemProperties = new TreeMap<>();
    }

    includedSystemProperties.put(systemProperty, null);
  }

  public void removeSystemProperty(String systemProperty) {
    if (includedSystemProperties != null) {
      includedSystemProperties.remove(systemProperty);
    }
  }

  /**
   * Add the additional Splunk fields to the event.
   *
   * <p>This method is called by the 'build' method.
   *
   * @param eventObject the target eventObject for the field values
   */
  protected void serializeFields(Map<String, Object> eventObject) {
    log.debug("Serializing event fields");

    putIfValueIsNotNull(eventObject, HOST_KEY, host);
    putIfValueIsNotNull(eventObject, SOURCE_KEY, source);
    putIfValueIsNotNull(eventObject, SOURCETYPE_KEY, sourcetype);
    putIfValueIsNotNull(eventObject, INDEX_KEY, index);

    if (hasTimestamp()) {
      eventObject.put(TIMESTAMP_KEY, String.format("%.3f", timestamp));
    }

    if (hasIncludedSystemProperties()) {
      for (Map.Entry<String, String> systemProperty : includedSystemProperties.entrySet()) {
        String systemPropertyName = systemProperty.getKey();
        if (systemPropertyName != null || !systemPropertyName.isEmpty()) {
          String systemPropertyValue = System.getProperty(systemPropertyName, null);
          if (systemPropertyValue != null && !systemPropertyValue.isEmpty()) {
            String splunkFieldName = systemProperty.getValue();
            if (splunkFieldName == null || splunkFieldName.isEmpty()) {
              splunkFieldName = systemPropertyName;
              systemProperty.setValue(systemPropertyName);
            }
            if (fields == null) {
              fields = new HashMap<>();
            }

            fields.put(splunkFieldName, systemPropertyValue);
          }
        }
      }

    }

    if (hasFields()) {
      eventObject.put(FIELDS_KEY, fields);
    }
  }

  /**
   * Add the body to the event.
   *
   * <p>This method is called by the 'build' method.
   *
   * @param eventObject the target eventObject Map&lt;String,Object&gt; for the body
   */
  protected void serializeBody(Map<String, Object> eventObject) {
    log.debug("Serializing event body");
    eventObject.put(EVENT_BODY_KEY, eventBody);
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
  protected void putIfValueIsNotNull(Map<String, Object> eventObject,
                                     final String key, final String value) {
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

  protected void copyConfiguration(JacksonEventBuilderSupport<E> sourceEventBuilder) {
    this.timestamp = sourceEventBuilder.timestamp;
    this.host = sourceEventBuilder.host;
    this.source = sourceEventBuilder.source;
    this.sourcetype = sourceEventBuilder.sourcetype;
    this.index = sourceEventBuilder.index;

    this.setIncludedSystemProperties(sourceEventBuilder.includedSystemProperties);
  }
}
