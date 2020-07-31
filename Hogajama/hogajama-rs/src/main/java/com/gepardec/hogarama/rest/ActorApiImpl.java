package com.gepardec.hogarama.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gepardec.hogarama.domain.watering.ActorControlService;

public class ActorApiImpl implements ActorApi {

	@Inject
	ActorControlService pumpService;

	@Override
	public Response sendActorMessage(String location, String actorName, Integer duration,
			SecurityContext securityContext) {
		pumpService.sendActorMessage(location, actorName, duration);
		return Response.ok().build();
	}
}
