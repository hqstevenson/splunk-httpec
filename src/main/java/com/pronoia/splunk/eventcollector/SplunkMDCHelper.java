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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public abstract class SplunkMDCHelper implements AutoCloseable {
    public static final String MDC_SPLUNK_HTTPEC_HOST = "splunk.httpec.host";
    public static final String MDC_SPLUNK_HTTPEC_PORT = "splunk.httpec.port";
    public static final String MDC_SPLUNK_HTTPEC_AUTHORIZATION_TOKEN = "splunk.httpec.token";

    public static final String MDC_SPLUNK_DEFAULT_HOST = "splunk.default.host";
    public static final String MDC_SPLUNK_DEFAULT_INDEX = "splunk.default.index";
    public static final String MDC_SPLUNK_DEFAULT_SOURCE = "splunk.default.source";
    public static final String MDC_SPLUNK_DEFAULT_SOURCETYPE = "splunk.default.sourcetype";

    public static final String MDC_SPLUNK_HOST = "splunk.host";
    public static final String MDC_SPLUNK_INDEX = "splunk.index";
    public static final String MDC_SPLUNK_SOURCE = "splunk.source";
    public static final String MDC_SPLUNK_SOURCETYPE = "splunk.sourcetype";

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected Map<String, String> contextMap;

    @SuppressWarnings("checkstyle:booleanexpressioncomplexity")
    protected void addEventBuilderValues(EventBuilder<?> eventBuilder) {
        if (eventBuilder != null) {
            if (eventBuilder.hasHost() || eventBuilder.hasDefaultHost()
                    || eventBuilder.hasIndex() || eventBuilder.hasDefaultIndex()
                    || eventBuilder.hasSource() || eventBuilder.hasDefaultSource()
                    || eventBuilder.hasDefaultSourcetype() || eventBuilder.hasDefaultSourcetype())  {

                saveContextMap();

                if (eventBuilder.hasHost()) {
                    MDC.put(MDC_SPLUNK_HOST, eventBuilder.getHost());
                }
                if (eventBuilder.hasDefaultHost()) {
                    MDC.put(MDC_SPLUNK_DEFAULT_HOST, eventBuilder.getDefaultHost());
                }

                if (eventBuilder.hasIndex()) {
                    MDC.put(MDC_SPLUNK_INDEX, eventBuilder.getIndex());
                }
                if (eventBuilder.hasDefaultIndex()) {
                    MDC.put(MDC_SPLUNK_DEFAULT_INDEX, eventBuilder.getDefaultIndex());
                }

                if (eventBuilder.hasSource()) {
                    MDC.put(MDC_SPLUNK_SOURCE, eventBuilder.getSource());
                }
                if (eventBuilder.hasDefaultSource()) {
                    MDC.put(MDC_SPLUNK_DEFAULT_SOURCE, eventBuilder.getDefaultSource());
                }

                if (eventBuilder.hasSourcetype()) {
                    MDC.put(MDC_SPLUNK_SOURCETYPE, eventBuilder.getSourcetype());
                }
                if (eventBuilder.hasDefaultSourcetype()) {
                    MDC.put(MDC_SPLUNK_DEFAULT_SOURCETYPE, eventBuilder.getDefaultSourcetype());
                }
            }
        }
    }

    @Override
    public void close() {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

    /**
     * Store a copy of the current MDC context map.
     *
     * If the MDC context map hasn't been captured, store a copy in the MDC helper.  Otherwise, keep the current copy.
     */
    protected void saveContextMap() {
        if (contextMap != null) {
            contextMap = MDC.getCopyOfContextMap();
        }
    }

}
