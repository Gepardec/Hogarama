package com.gepardec.hogarama.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gepardec.hogarama.domain.watering.ActorService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

public class ActorApiImpl implements ActorApi {

	@Inject
	ActorService pumpService;

	@Context
	SecurityContext sc;

	@Override
	public Response sendActorMessage(String location, String actorName, Integer duration,
			SecurityContext securityContext) {
		pumpService.sendActorMessage(location, actorName, duration);
		String username = sc.getUserPrincipal().getName();
		if (sc.getUserPrincipal() instanceof KeycloakPrincipal) {
			KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>)  sc.getUserPrincipal();

			// this is how to get the real userName (or rather the login name)
			username = kp.getKeycloakSecurityContext().getToken().getName();
		}
		return Response.ok().build();
	}
}
