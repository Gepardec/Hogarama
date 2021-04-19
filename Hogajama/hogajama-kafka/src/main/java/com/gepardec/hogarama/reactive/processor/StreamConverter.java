package com.gepardec.hogarama.reactive.processor;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StreamConverter {

  private static final Logger LOG = LoggerFactory.getLogger(StreamConverter.class);

  @Incoming("transformed-sensor-events")
  @Outgoing("rest-stream")
  @Broadcast
  @SuppressWarnings("unused")
  public String newValue(String valueJson) {
    LOG.info("Received event on stream converter: {}", valueJson);
    return valueJson;
  }

}
