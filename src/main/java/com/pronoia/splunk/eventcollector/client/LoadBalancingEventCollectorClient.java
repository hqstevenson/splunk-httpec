/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pronoia.splunk.eventcollector.client;

import com.pronoia.splunk.eventcollector.EventCollectorClient;
import com.pronoia.splunk.eventcollector.EventCollectorInfo;
import com.pronoia.splunk.eventcollector.EventDeliveryException;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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
