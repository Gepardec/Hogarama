package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/unitmanagement/sensor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SensorApi {

    @GET
    Response getForOwner(@Context SecurityContext securityContext);

    @PUT
    Response create(@Context SecurityContext securityContext, SensorDto sensorDto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, @Context SecurityContext securityContext, SensorDto sensorDto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id, @Context SecurityContext securityContext);
}
