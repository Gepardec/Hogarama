package com.gepardec.hogarama.domain.watering;

public interface ActorService {
  void sendActorMessage(String location, String sensorName, Integer duration);

}
