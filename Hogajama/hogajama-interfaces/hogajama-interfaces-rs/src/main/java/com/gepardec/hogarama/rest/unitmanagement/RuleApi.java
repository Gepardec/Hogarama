package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/unitmanagement/rule")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RuleApi {

    @GET
    Response getForUser(@Context SecurityContext securityContext);

    @PUT
    Response create(@Context SecurityContext securityContext, RuleDto actorDto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, @Context SecurityContext securityContext, RuleDto actorDto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id, @Context SecurityContext securityContext);
}
