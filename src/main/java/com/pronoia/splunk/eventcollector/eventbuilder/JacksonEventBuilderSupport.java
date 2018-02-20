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
package com.pronoia.splunk.eventcollector.eventbuilder;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JacksonEventBuilderSupport<E> extends EventBuilderSupport<E> {
    ObjectMapper jacksonObjectMapper = new ObjectMapper();

    @Override
    protected String convertMapToJson(Map<String, Object> map) {
        try {
            String jsonString = jacksonObjectMapper.writeValueAsString(map);
            log.debug("Converted Map<String, Object> '{}' to JSON '{}'", map, jsonString);
            return jsonString;
        } catch (JsonProcessingException jsonProcessingEx) {
            String errorMessage = String.format("Failed to create JSON from Map<String, Object>: %s",
                map != null ? map.toString() : "null");
            throw new IllegalStateException(errorMessage, jsonProcessingEx);
        }
    }

}
