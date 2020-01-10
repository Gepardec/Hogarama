package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.v2.dto.ActorDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("v2/actor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ActorApi {

    @POST
    Response createActor(@Context SecurityContext securityContext, ActorDto actorDto);

    @GET
    Response getActorsForOwner(@Context SecurityContext securityContext);
}
