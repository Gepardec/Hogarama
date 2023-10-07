package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RuleApi {

    @GET
    Response getForUser();

    @PUT
    Response create(RuleDto actorDto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, RuleDto actorDto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
