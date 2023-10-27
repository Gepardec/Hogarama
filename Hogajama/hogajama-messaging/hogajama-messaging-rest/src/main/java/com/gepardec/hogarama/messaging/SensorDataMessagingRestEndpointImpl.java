package com.gepardec.hogarama.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.watering.WateringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class SensorDataMessagingRestEndpointImpl implements SensorDataMessagingRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataMessagingRestEndpointImpl.class);

    @Inject
    WateringService wateringService;

    @Override
    public Response putMessage(SensorData sensorData) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(sensorData);
            LOGGER.info("Ongoing sensor data {}", json);
            wateringService.processSensorData(sensorData);
        } catch (Exception e) {
            throw new RestMessagingException(e);
        }
        return Response.ok().build();
    }
}
