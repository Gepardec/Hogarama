package com.gepardec.hogarama.service;

public interface PumpService {
  void sendPumpMessage(String location, String sensorName, Integer duration);

}
