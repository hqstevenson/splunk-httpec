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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;


/**
 * Interface for the EventBuilder used by the HTTP Event Collector to serialize
 * events before delivering them to Splunk.
 *
 * @param <E> The type of the event body
 */
public interface EventBuilder<E> {
    /**
     * Determine if the instance has a Splunk 'host' default field configured.
     *
     * @return true if 'host' field is configured; false otherwise
     */
    boolean hasDefaultHost();


    /**
     * Get the default value of the Splunk 'host' default field for the event that will
     * be used on the next invocation of the build() method.
     *
     * @return the Splunk 'host' default field value
     */
    String getDefaultHost();


    /**
     * Set the default value for the Splunk 'host' default field that will be used on the
     * next invocation of the build() method.
     *
     * <p>This value is typically a hostname, IP address or fully qualified
     * domain name of the host from which the event originated.
     *
     * @param host value for the 'host' default field.
     */
    void setDefaultHost(String host);


    /**
     * Determine and set the default value for the Splunk 'host' default field that will
     * be used on the next invocation of the build() method.
     *
     * <p>This method sets the 'host' value to the value returned from
     * InetAddress.getLocalHost().getHostName()
     */
    default void setDefaultHost() {
        try {
            this.setDefaultHost(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException unknowHostException) {
            // Ignore
        }
    }


    /**
     * Determine if the instance has a default  Splunk 'index' default field configured.
     *
     * @return true if 'index' field is configured; false otherwise
     */
    boolean hasDefaultIndex();


    /**
     * Get the name of Splunk 'index' default field for the event that will be
     * used on the next invocation of the build() method.
     *
     * @return the Splunk 'index' field value
     */
    String getDefaultIndex();


    /**
     * Set the default value for the Splunk 'index' default field that will be used on
     * the next invocation of the build() method.
     *
     * <p>NOTE:  The Splunk Index must be created in the Splunk environment prior to
     * use.  Using in invalid value for the Splunk 'index' default field may
     * result in data loss.
     *
     * @param index value of the Splunk Index field
     */
    void setDefaultIndex(String index);


    /**
     * Determine if the instance has a Splunk 'source' default field configured.
     *
     * @return true if 'source' field is configured; false otherwise
     */
    boolean hasDefaultSource();


    /**
     * Get the default value of Splunk 'source' default field for the event that will be
     * used on the next invocation of the build() method.
     *
     * @return the Splunk 'source' default field value
     */
    String getDefaultSource();


    /**
     * Set the default value for the Splunk 'source' default field that will be used on
     * the next invocation of the build() method.
     *
     * <p>This value is typically the name of the file, stream or other input from
     * which the event originates.
     *
     * @param source value for the 'source' default field.
     */
    void setDefaultSource(String source);


    /**
     * Determine if the instance has a Splunk 'sourcetype' default field
     * configured.
     *
     * @return true if 'sourcetype' field is configured; false otherwise
     */
    boolean hasDefaultSourcetype();


    /**
     * Get the default value of Splunk 'sourcetype' default field for the event that will
     * be used on the next invocation of the build() method.
     *
     * @return the Splunk 'sourcetype' default field value
     */
    String getDefaultSourcetype();


    /**
     * Set the default value for the Splunk 'sourcetype' default field that will be used
     * on the next invocation of the build() method.
     *
     * <p>This value is typically a name used to identify the format of the data in
     * the event.
     *
     * @param defaultSourcetype default value for the 'sourcetype' default field.
     */
    void setDefaultSourcetype(String defaultSourcetype);


    /**
     * Determine if the instance has a Splunk 'host' default field configured.
     *
     * @return true if 'host' field is configured; false otherwise
     */
    boolean hasHost();


    /**
     * Get the value of the Splunk 'host' default field for the event that will
     * be used on the next invocation of the build() method.
     *
     * @return the Splunk 'host' default field value
     */
    String getHost();


    /**
     * Set the value for the Splunk 'host' default field that will be used on the
     * next invocation of the build() method.
     *
     * <p>This value is typically a hostname, IP address or fully qualified
     * domain name of the host from which the event originated.
     *
     * @param host value for the 'host' default field.
     */
    void setHost(String host);


    /**
     * Determine and set the value for the Splunk 'host' default field that will
     * be used on the next invocation of the build() method.
     *
     * <p>This method sets the 'host' value to the value returned from
     * InetAddress.getLocalHost().getHostName()
     */
    default void setHost() {
        try {
            this.setHost(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException unknowHostException) {
            // Ignore
        }
    }


    /**
     * Determine if the instance has a Splunk 'index' default field configured.
     *
     * @return true if 'index' field is configured; false otherwise
     */
    boolean hasIndex();


    /**
     * Get the name of Splunk 'index' default field for the event that will be
     * used on the next invocation of the build() method.
     *
     * @return the Splunk 'index' field value
     */
    String getIndex();


    /**
     * Set the value for the Splunk 'index' default field that will be used on
     * the next invocation of the build() method.
     *
     * <p>NOTE:  The Splunk Index must be created in the Splunk environment prior to
     * use.  Using in invalid value for the Splunk 'index' default field may
     * result in data loss.
     *
     * @param index value of the Splunk Index field
     */
    void setIndex(String index);


    /**
     * Determine if the instance has a Splunk 'source' default field configured.
     *
     * @return true if 'source' field is configured; false otherwise
     */
    boolean hasSource();


    /**
     * Get the value of Splunk 'source' default field for the event that will be
     * used on the next invocation of the build() method.
     *
     * @return the Splunk 'source' default field value
     */
    String getSource();


    /**
     * Set the value for the Splunk 'source' default field that will be used on
     * the next invocation of the build() method.
     *
     * <p>This value is typically the name of the file, stream or other input from
     * which the event originates.
     *
     * @param source value for the 'source' default field.
     */
    void setSource(String source);


    /**
     * Determine if the instance has a Splunk 'sourcetype' default field
     * configured.
     *
     * @return true if 'sourcetype' field is configured; false otherwise
     */
    boolean hasSourcetype();


    /**
     * Get the value of Splunk 'sourcetype' default field for the event that will
     * be used on the next invocation of the build() method.
     *
     * @return the Splunk 'sourcetype' default field value
     */
    String getSourcetype();


    /**
     * Set the value for the Splunk 'sourcetype' default field that will be used
     * on the next invocation of the build() method.
     *
     * <p>This value is typically a name used to identify the format of the data in
     * the event.
     *
     * @param sourcetype value for the 'sourcetype' default field.
     */
    void setSourcetype(String sourcetype);


    /**
     * Determine if the instance has a Splunk 'timestamp' configured.
     *
     * @return true if 'timestamp' field is configured; false otherwise
     */
    boolean hasTimestamp();


    /**
     * Get the value of Splunk 'timestamp' default field for the event that will
     * be used on the next invocation of the build() method.
     *
     * @return the Splunk 'timestamp' default field value
     */
    Double getTimestamp();


    /**
     * Set the value for the Splunk 'timestamp' default field that will be used
     * on the next invocation of the build() method.
     *
     * <p>This value is typically the time at which the event occurred.
     *
     * @param date value for the 'timestamp' default field.
     */
    default void setTimestamp(Date date) {
        final double millisecondsPerSecond = 1000.0;

        this.setTimestamp(date.getTime() / millisecondsPerSecond);
    }


    /**
     * Set the value for the Splunk 'timestamp' default field that will be used
     * on the next invocation of the build() method.
     *
     * <p>This value is typically the time at which the event occurred.
     *
     * @param epochSeconds value for the 'timestamp' default field as the number of seconds since 1/1/1970 GMT.
     */
    void setTimestamp(double epochSeconds);


    /**
     * Set the value for the Splunk 'timestamp' default field that will be used on
     * the next invocation of the build() method.
     *
     * <p>This value is typically the time at which the event occurred.
     *
     * @param epochMilliseconds value for the 'timestamp' default field as the number of milliseconds since 1/1/1970 GMT.
     */
    default void setTimestamp(long epochMilliseconds) {
        final double millisecondsPerSecond = 1000.0;

        this.setTimestamp(epochMilliseconds / millisecondsPerSecond);
    }


    /**
     * Set the value for the Splunk 'timestamp' default field that will be used on
     * the next invocation of the build() method.
     *
     * <p>This method sets the 'timestamp' value to the value returned from System.currentTImeMillis().
     */
    default void setTimestamp() {
        final double millisecondsPerSecond = 1000.0;

        this.setTimestamp(System.currentTimeMillis() / millisecondsPerSecond);
    }

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
     * Determine if the instance has an event configured.
     *
     * @return true if an event has been configured; false otherwise
     */
    boolean hasEventBody();


    /**
     * Get the body of the event that will be used on the next invocation of the
     * build() method.
     *
     * @return the event body
     */
    E getEventBody();


    /**
     * Set the body of the event (the _raw field) that will be used on the next
     * invocation of the build() method.
     *
     * @param eventBody the body of the event
     */
    void setEventBody(E eventBody);


    /**
     * Determine if the instance has additional Splunk fields configured.
     *
     * @return true if additional Splunk fields are configured; false otherwise
     */
    boolean hasFields();


    /**
     * Get the map of additional field names and values that will be used on the
     * next invocation of the build() method.
     *
     * @return the map of field names and values
     */
    Map<String, Object> getFields();


    /**
     * Set the map of field names and values that will be used on the
     * next invocation of the build() method.
     *
     * <p>NOTE:  Additional indexed fields are only supported on Splunk 6.5 or
     * greater, and INDEXED_EXTRACTION must be set to JSON for the sourcetype
     * in props.conf.
     *
     * @param fieldMap the map of field names and values
     */
    void setFields(Map<String, Object> fieldMap);


    /**
     * Set the value of a indexed field that will be used on the next invocation
     * of the build() method.
     *
     * <p>NOTE:  Additional indexed Fields are only supported on Splunk 6.5 or
     * greater, and INDEXED_EXTRACTION must be set to JSON for the sourcetype
     * in props.conf.
     *
     * @param fieldName   the name of the indexed field.
     * @param fieldValues the value(s) of the indexed field
     */
    void setField(String fieldName, String... fieldValues);


    /**
     * Add the map of additional field names and values that will be used on the
     * next invocation of the build() method.
     *
     * <p>NOTE:  Additional indexed fields are only supported on Splunk 6.5 or
     * greater, and INDEXED_EXTRACTION must be set to JSON for the sourcetype
     * in props.conf.
     *
     * @param fieldMap the map of field names and values
     */
    void addFields(Map<String, Object> fieldMap);


    /**
     * Add the value of a indexed field that will be used on the next invocation
     * of the build() method.
     *
     * <p>NOTE:  Additional indexed Fields are only supported on Splunk 6.5 or
     * greater, and INDEXED_EXTRACTION must be set to JSON for the sourcetype
     * in props.conf.
     *
     * @param fieldName   the name of the indexed field.
     * @param fieldValues the value(s) of the indexed field
     */
    void addField(String fieldName, String... fieldValues);


    /**
     * Clear the map of additional fields that will be used on the next
     * invocation of the build() method using a fluent builder.
     *
     * @return the current EventBuilder
     */
    EventBuilder<E> clearFields();


    /**
     * Set the value for the Splunk 'host' default field using a fluent builder
     * pattern.
     *
     * <p>See setHost() for details.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> host() {
        this.setHost();
        return this;
    }


    /**
     * Set the value for the Splunk 'host' default field using a fluent builder
     * pattern.
     *
     * <p>See setHost(String) for details.
     *
     * @param host value for the 'host' default field.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> host(String host) {
        this.setHost(host);
        return this;
    }


    /**
     * Set the value for the Splunk Index using a fluent builder pattern.
     *
     * <p>See setIndex(String) for details.
     *
     * @param index name of the Splunk Index
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> index(String index) {
        this.setIndex(index);
        return this;
    }


    /**
     * Set the value for the Splunk 'source' default field using a fluent builder
     * pattern.
     *
     * <p>See setSource(String) for details.
     *
     * @param source value for the 'source' default field.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> source(String source) {
        this.setSource(source);
        return this;
    }


    /**
     * Set the value for the Splunk 'source' default field using a fluent builder
     * pattern.
     *
     * <p>See setSourcetype(String) for details.
     *
     * @param sourcetype value for the 'source' default field.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> sourcetype(String sourcetype) {
        this.setSourcetype(sourcetype);
        return this;
    }


    /**
     * Set the value for the Splunk 'timestamp' default field using a builder
     * pattern.
     *
     * <p>See the setTimestamp(double) method for details.
     *
     * @param epochSeconds value for the 'timestamp' default field as the number of seconds since 1/1/1970 GMT.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> timestamp(double epochSeconds) {
        this.setTimestamp(epochSeconds);
        return this;
    }


    /**
     * Set the value for the Splunk 'timestamp' default field using a builder
     * pattern.
     *
     * <p>See the setTimestamp(long) method for details.
     *
     * @param epochMilliseconds value for the 'timestamp' default field as the number of milliseconds since 1/1/1970 GMT.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> timestamp(long epochMilliseconds) {
        this.setTimestamp(epochMilliseconds);
        return this;
    }


    /**
     * Set the value for the Splunk 'timestamp' default field using a builder
     * pattern.
     *
     * <p>See the setTimestamp(Date) method for details.
     *
     * @param date value for the 'timestamp' default field.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> timestamp(Date date) {
        this.setTimestamp(date);
        return this;
    }


    /**
     * Set the value for the Splunk 'timestamp' default field using a builder
     * pattern.
     *
     * <p>See the setTimestamp() method for details.
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> timestamp() {
        this.setTimestamp();
        return this;
    }


    /**
     * Set the body of the Splunk event using a builder pattern.
     *
     * <p>See the setEvent(E) method for details.
     *
     * @param eventBody the body of the event
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> eventBody(E eventBody) {
        this.setEventBody(eventBody);
        return this;
    }


    /**
     * Set the map of additional indexed field names and values using a
     * fluent builder pattern.
     *
     * <p>See the setFields(Map) method for details.
     *
     * @param fieldMap the map of field names and values
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> fields(Map<String, Object> fieldMap) {
        this.setFields(fieldMap);
        return this;
    }


    /**
     * Set the value of a indexed field using a builder pattern.
     *
     * <p>See setField(String, String...) for details.
     *
     * @param fieldName   the name of the indexed field.
     * @param fieldValues the value(s) of the indexed field
     *
     * @return the current EventBuilder
     */
    default EventBuilder<E> field(String fieldName, String... fieldValues) {
        this.setField(fieldName, fieldValues);
        return this;
    }


    /**
     * Build the JSON-formatted event suitable for the Splunk HTTP Event
     * Collector.
     *
     * @param client the event collector client
     *
     * @return the JSON-formatted event
     */
    String build(EventCollectorClient client);


    /**
     * Create a duplicate EventBuilder.
     *
     * @return a duplicate of this
     */
    EventBuilder<E> duplicate();

}
