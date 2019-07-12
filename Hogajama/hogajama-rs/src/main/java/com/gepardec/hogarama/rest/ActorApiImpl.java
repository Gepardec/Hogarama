package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.domain.watering.ActorService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class ActorApiImpl implements ActorApi {

    @Inject
    ActorService pumpService;

    @Override
    public Response sendActorMessage(String location, String actorName, Integer duration,
                                     SecurityContext securityContext) {
        pumpService.sendActorMessage(location, actorName, duration);
        return Response.ok().build();
    }
}
