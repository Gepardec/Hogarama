package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.v2.dto.BaseDTO;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ApiBase<DTO extends BaseDTO> {

    @RolesAllowed("admins")
    @Path("all")
    @GET
    Response getAll(@Context SecurityContext securityContext);

    @GET
    Response getForOwner(@Context SecurityContext securityContext);

    @PUT
    Response create(@Context SecurityContext securityContext, DTO dto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, @Context SecurityContext securityContext, DTO dto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id, @Context SecurityContext securityContext);
}
