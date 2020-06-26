package com.gepardec.hogarama.domain.watering;

// TODO do we want to integrate this service into the new unitmanagement ActorService
public interface SendActorMessageService {
  void sendActorMessage(String location, String sensorName, Integer duration);

}
