package com.gepardec.hogarama.reactive.producer;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class EventGeneratorSimba {

  @Inject
  private MockExternalAsyncResource externalAsyncResource;

  @Outgoing("sensor-events")
  @SuppressWarnings("unused")
  public CompletionStage<String> generate() {
    return externalAsyncResource.getNextValue("simba");
  }

}
