package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.v2.dto.ActorDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("v2/pump")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ActorApi {

    @POST
    Response createActor(@Context SecurityContext securityContext, ActorDto actorDto);
}
