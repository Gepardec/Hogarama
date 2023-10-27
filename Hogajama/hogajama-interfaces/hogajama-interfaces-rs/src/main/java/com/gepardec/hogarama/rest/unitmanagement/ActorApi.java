package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.ActorDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ActorApi {

    @GET
    Response getForUser();

    @PUT
    Response create(ActorDto actorDto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, ActorDto actorDto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
