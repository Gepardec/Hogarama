package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SensorApi {

    @GET
    Response getForUser();

    @PUT
    Response create(SensorDto sensorDto);

    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, SensorDto sensorDto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
