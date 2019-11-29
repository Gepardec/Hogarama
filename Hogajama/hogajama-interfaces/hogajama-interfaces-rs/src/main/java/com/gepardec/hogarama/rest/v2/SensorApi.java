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
    Response getSensorForCurrentOwner(@Context SecurityContext securityContext);

    @POST
    Response createSensor(@Context SecurityContext securityContext, SensorDto sensorDto);


}
