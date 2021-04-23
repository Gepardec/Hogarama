package com.gepardec.hogarama.reactive.consumer;

import com.gepardec.hogarama.reactive.vo.Event;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class TransformedEventConsumerAlpha {

  private static final Logger LOG = LoggerFactory.getLogger(TransformedEventConsumerAlpha.class);

  @Incoming("transformed-sensor-events-in")
  @SuppressWarnings("unused")
  public String newValue(String valueJson) {
    LOG.info("Received transformed event on consumer alpha: {}", valueJson);
    Jsonb jsonb = JsonbBuilder.create();

    Event event = jsonb.fromJson(valueJson, Event.class);

    return jsonb.toJson(event);
  }
}
