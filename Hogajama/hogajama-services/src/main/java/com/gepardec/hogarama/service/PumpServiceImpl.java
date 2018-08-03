package com.gepardec.hogarama.service;

import com.gepardec.hogarama.mocks.cli.MqttClient;
import com.gepardec.hogarama.service.dao.HabaramaDAOImpl;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Optional;

public class PumpServiceImpl implements PumpService {

  JSONObject json = new JSONObject();

  @Inject
  private HabaramaDAOImpl habaramaDao;

  public void sendPumpMessage(String location, String sensorName, Integer duration){

    // TODO activate when MongoDB is working
    // checkParametersOrFail(location, sensorName, duration);

    MqttClient mqttClient = new MqttClient().
      withHost(Optional.ofNullable(System.getenv("AMQ_HOST")).orElse("https://broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at")).
      withUser(Optional.ofNullable(System.getenv("AMQ_USER")).orElse("mq_habarama")).
      withPassword(Optional.ofNullable(System.getenv("AMQ_PASSWDORD")).orElse("mq_habarama_pass")).
      withTopic(Optional.ofNullable(System.getenv("AMQ_TOPICS")).orElse("actor." + location + "." + sensorName)).
      build();
    try {
      json.put("name", sensorName);
      json.put("location", location);
      json.put("duration", duration);
    } catch (Exception e){
      throw new RuntimeException("Error creating JSONObject.");
    }

    String message = json.toString();
    mqttClient.connectAndPublish(message);
  }

  private void checkParametersOrFail(String location, String sensorName, Integer duration) {
    if(location == null || location.isEmpty() || sensorName == null || sensorName.isEmpty() || duration == null) {
      throw new IllegalArgumentException(String.format("Supplied parameters '%s', '%s', '%s' must not be empty or null", location, sensorName, duration));
    }

    String registeredLocation = habaramaDao.getLocationBySensorName(sensorName);
    if(registeredLocation == null) {
      throw new IllegalArgumentException(sensorName + " is not a registered sensor.");
    }
    if(!registeredLocation.equals(location)) {
      throw new IllegalArgumentException(String.format("For sensor %s location must be '%s' but was '%s'", sensorName, registeredLocation, location));
    }

  }



}
