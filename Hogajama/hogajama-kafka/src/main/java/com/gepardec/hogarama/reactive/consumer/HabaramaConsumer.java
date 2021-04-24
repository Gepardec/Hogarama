package com.gepardec.hogarama.reactive.consumer;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class HabaramaConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(HabaramaConsumer.class);

  @Incoming("habarama-in")
  public void newValue(String msg) {
    LOG.info("Received habarama sensor values: {}", msg);
  }
}
