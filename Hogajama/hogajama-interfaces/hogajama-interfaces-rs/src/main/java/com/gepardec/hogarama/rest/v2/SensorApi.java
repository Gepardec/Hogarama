package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.v2.dto.SensorDto;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("v2/sensor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SensorApi {

    @RolesAllowed("admins")
    @Path("all")
    @GET
    Response getAllSensors(@Context SecurityContext securityContext);

    @GET
    Response getSensorsForOwner(@Context SecurityContext securityContext);

    @PUT
    Response createSensor(@Context SecurityContext securityContext, SensorDto sensorDto);

    @PATCH
    @Path("/{id}")
    Response updateSensor(@PathParam("id") String id, @Context SecurityContext securityContext, SensorDto sensorDto);

    @DELETE
    @Path("/{id}")
    Response deleteSensor(@PathParam("id") String id, @Context SecurityContext securityContext);
}
