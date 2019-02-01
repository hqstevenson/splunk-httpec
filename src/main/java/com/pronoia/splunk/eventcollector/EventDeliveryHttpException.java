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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventDeliveryHttpException extends EventDeliveryException {
    static final Logger LOG = LoggerFactory.getLogger(EventDeliveryHttpException.class);

    final int httpStatusCode;
    final String httpReasonPhrase;
    final Map<String, Object> splunkResponse;

    public EventDeliveryHttpException(String event, HttpResponse response, String responseBody) {
        super(event, String.format("Post failed with response %s", responseBody));

        this.httpStatusCode = response.getStatusLine().getStatusCode();
        this.httpReasonPhrase = response.getStatusLine().getReasonPhrase();
        this.splunkResponse = parseResponseBody(responseBody);
    }

    public EventDeliveryHttpException(String event, HttpResponse response) {
        this(event, response, extractResponseBody(response));
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getHttpReasonPhrase() {
        return httpReasonPhrase;
    }

    /**
     * Get the status code returned from Splunk.
     *
     * @return the status code extracted from the HTTP response, or -1 if the code could not be extracted.
     *
     * @see <a href="https://docs.splunk.com/Documentation/Splunk/latest/Data/TroubleshootHTTPEventCollector#Possible_error_codes">Splunk HTTP Event Collector Documentation</a>
     */
    public int getSplunkStatusCode() {
        int answer = -1;
        if (splunkResponse.containsKey("code")) {
            Object codeObj = splunkResponse.get("code");
            try {
                answer = Integer.parseInt(codeObj.toString());
            } catch (Exception parseEx) {
                LOG.warn("Failed to parse integer from %s - returning %d", codeObj, answer);
            }
        }
        return answer;
    }

    public String getSplunkStatusMessage() {
        String answer = "<unknown>";

        if (splunkResponse.containsKey("text")) {
            Object textObj = splunkResponse.get("text");
            if (textObj != null) {
                answer = textObj.toString();
            }
        }
        return answer;
    }

    static String extractResponseBody(HttpResponse response) {
        String responseBody;

        if (response == null) {
            responseBody = "<null HttpResponse>";
        } else {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null) {
                responseBody = "<null HttpEntity>";
            } else {
                try {
                    responseBody = EntityUtils.toString(responseEntity);
                    if (responseBody == null) {
                        responseBody = "<null response body>";
                    } else if (responseBody.isEmpty()) {
                        responseBody = "<empty response body>";
                    }
                } catch (IOException entityEx) {
                    responseBody = String.format("<exception getting response body - %s: %s>", entityEx.getClass().getSimpleName(), entityEx.getMessage());
                }
            }
        }

        return responseBody;
    }

    static Map<String, Object> parseResponseBody(String responseBody) {
        Map<String, Object> answer;
        ObjectMapper mapper = new ObjectMapper();

        try {
            answer = mapper.readValue(responseBody, new TypeReference<Map<String, String>>() {});
        } catch (IOException parseEx) {
            LOG.warn("Exception encountered parsing JSON string", parseEx);
            answer = new HashMap<>();
        }

        return answer;
    }

}
