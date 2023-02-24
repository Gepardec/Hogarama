package com.gepardec.hogarama.messaging;

import com.gepardec.hogarama.domain.sensor.SensorData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/messaging/sensorData")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public interface SensorDataMessagingRestEndpoint {

    @POST
    Response putMessage(SensorData sensorData);
}