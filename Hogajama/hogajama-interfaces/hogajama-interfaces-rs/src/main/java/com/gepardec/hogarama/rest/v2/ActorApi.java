package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.v2.dto.ActorDto;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("v2/actor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ActorApi {
    @RolesAllowed("admins")
    @Path("all")
    @GET
    Response getAllActors(@Context SecurityContext securityContext);

    @GET
    Response getActorsForOwner(@Context SecurityContext securityContext);

    @PUT
    Response createActor(@Context SecurityContext securityContext, ActorDto actorDto);

    @PATCH
    @Path("/{id}")
    Response updateActor(@PathParam("id") String id, @Context SecurityContext securityContext, ActorDto actorDto);

    @DELETE
    @Path("/{id}")
    Response deleteActor(@PathParam("id") String id, @Context SecurityContext securityContext);
}
