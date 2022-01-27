package com.gepardec.hogarama.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;

import com.gepardec.hogarama.domain.watering.WateringDataDAO;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.watering.ActorControlService;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.mocks.cli.MqttClient;

public class ActorControlServiceImpl implements ActorControlService {

    @Inject
    private WateringDataDAO dao;

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
        dao.save(data);
        mqttClient.connectAndPublish(new JSONObject(data).toString());
    }

    protected void checkParametersOrFail(String location, String actorName, Integer duration) {
        if (location == null || location.isEmpty() || actorName == null || actorName.isEmpty() || duration == null) {
            Metrics.exceptionsThrown.labels("hogarama_services", "IllegalArgumentException", "ActorServiceImpl.checkParametersOrFail").inc();
            throw new IllegalArgumentException(String.format("Supplied parameters '%s', '%s', '%s' must not be empty or null", location, actorName, duration));
        }
    }
}
