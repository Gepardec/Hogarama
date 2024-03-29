package com.gepardec.hogarama.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.DateUtils;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.watering.WateringService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@ApplicationScoped
public class SensorDataKafkaEndpoint {

    private static final Logger log = LoggerFactory.getLogger(SensorDataKafkaEndpoint.class);

    @Inject
    WateringService wateringSvc;

    @Transactional
    @Incoming("habarama-in")
    public void onMessage(String message) {
        log.info("Receive message from habarama-in: " + message);
        try {

            ObjectMapper mapper = new ObjectMapper();
            SensorData sensorData = mapper.readValue(message, SensorData.class);
            sensorData.setTime(DateUtils.toDate(LocalDateTime.now()));

            wateringSvc.processSensorData(sensorData);
        } catch (IOException e) {
            throw new RuntimeException("Error handling sensor data!", e);
        }
    }
}
