package com.gepardec.hogarama.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;

@ApplicationScoped
public class SensorSmoother {

  private static final Logger log = LoggerFactory.getLogger(SensorSmoother.class);

  private HashMap<String, Double> sensorDataCache = new HashMap<>();

  @Inject
  SensorNormalizer sensorNormalizer;

  @Incoming("habarama-raw-in")
  @Outgoing("habarama-smooth-out")
  public String onMessageRaw(String message) {

    log.info("Receive message from habarama-raw-in: " + message);

    try {

      ObjectMapper mapper = new ObjectMapper();
      SensorData sensorData = sensorNormalizer.normalize(mapper.readValue(message, SensorData.class));
      String sensorName = sensorData.getSensorName();

      sensorDataCache.computeIfAbsent(sensorData.getSensorName(), v -> sensorData.getValue());

      double avgSensorValue = computeAverageAndUpdateCache(sensorName, sensorData.getValue());
      sensorData.setValue(avgSensorValue);

      return mapper.writeValueAsString(sensorData);

    } catch (IOException e) {
      throw new RuntimeException("Error handling sensor data!", e);
    }
  }

  @Incoming("habarama-smooth-in")
  public void onMessageSmooth(String message) {

    log.info("Receive message from habarama-smooth-in: " + message);

  }

  private double computeAverageAndUpdateCache(String sensorName, double newValue) {
    Double cachedValue = sensorDataCache.get(sensorName);

    double avgValue = computeAverage(newValue, cachedValue);

    updateSensorCache(sensorName, avgValue);

    return avgValue;
  }

  private double computeAverage(Double cachedValue, double newValue) {
    if (null == cachedValue) {
      cachedValue = newValue;
    }

    return (cachedValue * 2 + newValue) / 3;
  }

  private void updateSensorCache(String sensorName, double avgValue) {
    sensorDataCache.put(sensorName, avgValue);
  }
}
