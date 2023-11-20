package com.gepardec.hogarama.messaging;

import com.gepardec.hogarama.domain.sensor.SensorData;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/messaging/sensorData")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public interface SensorDataMessagingRestEndpoint {

    @POST
    Response putMessage(SensorData sensorData);
}