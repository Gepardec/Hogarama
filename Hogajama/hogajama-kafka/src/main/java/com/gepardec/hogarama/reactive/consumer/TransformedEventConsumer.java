package com.gepardec.hogarama.reactive.consumer;

import com.gepardec.hogarama.reactive.vo.Event;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class TransformedEventConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(TransformedEventConsumer.class);

  @Incoming("transformedevents")

  //@Outgoing("in-memory-stream")
  //@Broadcast
  @SuppressWarnings("unused")
  public String newValue(String valueJson) {
    LOG.info("Received transformed event on consumer one: {}", valueJson);
    Jsonb jsonb = JsonbBuilder.create();

    Event event = jsonb.fromJson(valueJson, Event.class);

    return jsonb.toJson(event);
  }
}
