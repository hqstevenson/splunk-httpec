package com.pronoia.splunk.eventcollector.stub;

import com.pronoia.splunk.eventcollector.EventBuilder;
import com.pronoia.splunk.eventcollector.builder.EventBuilderSupport;

public class EventBuilderSupportStub extends EventBuilderSupport<String> {
  @Override
  public EventBuilder<String> duplicate() {
    EventBuilderSupportStub answer = new EventBuilderSupportStub();

    answer.copyConfiguration(this);

    return answer;
  }
}
