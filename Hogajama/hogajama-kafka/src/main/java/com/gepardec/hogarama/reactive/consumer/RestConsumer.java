package com.gepardec.hogarama.reactive.consumer;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/events")
public class RestConsumer {

  @Inject
  @Channel("rest-stream")
  Publisher<String> sensorEvent;

  @GET
  @Path("/stream")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @SseElementType("text/plain")
  public Publisher<String> stream() {
    return sensorEvent;
  }
}
