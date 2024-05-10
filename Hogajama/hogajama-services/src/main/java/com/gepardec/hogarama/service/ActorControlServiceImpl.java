package com.gepardec.hogarama.service;

import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;

import com.gepardec.hogarama.domain.watering.ActorControlService;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.mocks.cli.MqttClient;

import jakarta.inject.Inject;

public class ActorControlServiceImpl implements ActorControlService {

    @Inject
    private Logger log;


    @Override
    public void sendActorMessage(WateringData actorData) {

        log.info("sendActorMessage: {}", actorData.toString());

        MqttClient mqttClient = new MqttClient().defaultConnection().
                withTopic(Optional.ofNullable(System.getenv("AMQ_TOPICS")).orElse("actor." + actorData.getLocation() + "." + actorData.getName())).
                build();
        
        mqttClient.connectAndPublish(new JSONObject(actorData).toString());
    }

}
