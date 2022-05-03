package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/unitmanagement/unit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UnitApi {

    @GET
    Response getForOwner(@Context SecurityContext securityContext);

    @PUT
    Response create(@Context SecurityContext securityContext, UnitDto unitDto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, @Context SecurityContext securityContext, UnitDto unitDto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id, @Context SecurityContext securityContext);
}
