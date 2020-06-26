package com.gepardec.hogarama.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;

import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;

import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.watering.SendActorMessageService;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.mocks.cli.MqttClient;

public class SendActorMessageServiceImpl implements SendActorMessageService {

    @Inject
    private Datastore db;

    @Inject
    private Logger log;


    @Override
    public void sendActorMessage(String location, String actorName, Integer duration) {

        log.info("sendActorMessage: location: {}, actorName: {}, duration: {}", location, actorName, duration);
        checkParametersOrFail(location, actorName, duration);

        MqttClient mqttClient = new MqttClient().defaultConnection().
                withTopic(Optional.ofNullable(System.getenv("AMQ_TOPICS")).orElse("actor." + location + "." + actorName)).
                build();


        WateringData data = new WateringData(new Date(), actorName, location, duration);
        db.save(data);
        JSONObject json = new JSONObject(data);
        String message = json.toString();
        mqttClient.connectAndPublish(message);
    }

    protected void checkParametersOrFail(String location, String actorName, Integer duration) {
        if (location == null || location.isEmpty() || actorName == null || actorName.isEmpty() || duration == null) {
            Metrics.exceptionsThrown.labels("hogarama_services", "IllegalArgumentException", "ActorServiceImpl.checkParametersOrFail").inc();
            throw new IllegalArgumentException(String.format("Supplied parameters '%s', '%s', '%s' must not be empty or null", location, actorName, duration));
        }
    }
}
