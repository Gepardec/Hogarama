package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.domain.watering.WateringService;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

public class ActorApiImpl implements ActorApi {

	@Inject
	WateringService wateringService;

	@Override
	public Response sendActorMessage(String location, String actorName, Integer duration,
			SecurityContext securityContext) {
		wateringService.sendActorMessage(new WateringData(actorName, location, duration));
		return Response.ok().build();
	}
}
