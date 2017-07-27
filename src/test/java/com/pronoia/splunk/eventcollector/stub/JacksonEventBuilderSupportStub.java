package com.pronoia.splunk.eventcollector.stub;

import com.pronoia.splunk.eventcollector.EventBuilder;
import com.pronoia.splunk.eventcollector.eventbuilder.JacksonEventBuilderSupport;

public class JacksonEventBuilderSupportStub extends JacksonEventBuilderSupport<String> {
  @Override
  public EventBuilder<String> duplicate() {
    JacksonEventBuilderSupportStub answer = new JacksonEventBuilderSupportStub();

    answer.copyConfiguration(this);

    return answer;
  }

}
