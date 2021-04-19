package com.gepardec.hogarama.reactive.consumer;

import com.gepardec.hogarama.reactive.vo.Event;
import io.smallrye.reactive.messaging.annotations.Merge;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class TransformedEventConsumerBeta {

  private static final Logger LOG = LoggerFactory.getLogger(TransformedEventConsumerBeta.class);

  @Incoming("transformed-sensor-events")
  @SuppressWarnings("unused")
  public String newValue(String valueJson) {
    LOG.info("Received transformed event on consumer beta: {}", valueJson);
    Jsonb jsonb = JsonbBuilder.create();

    Event event = jsonb.fromJson(valueJson, Event.class);

    return jsonb.toJson(event);
  }
}
