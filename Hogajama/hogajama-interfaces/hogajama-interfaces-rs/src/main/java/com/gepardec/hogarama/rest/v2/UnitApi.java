package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.v2.dto.UnitDto;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("v2/unit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UnitApi {
    @RolesAllowed("admins")
    @Path("all")
    @GET
    Response getAllUnits(@Context SecurityContext securityContext);

    @GET
    Response getUnitsForOwner(@Context SecurityContext securityContext);

    @PUT
    Response createUnit(@Context SecurityContext securityContext, UnitDto unitDto);

    @PATCH
    @Path("/{id}")
    Response updateUnit(@PathParam("id") String id, @Context SecurityContext securityContext, UnitDto unitDto);

    @DELETE
    @Path("/{id}")
    Response deleteUnit(@PathParam("id") String id, @Context SecurityContext securityContext);
}
