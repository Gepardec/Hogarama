package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.watering.ActorService;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.mocks.cli.MqttClient;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;

public class ActorServiceImpl implements ActorService {


    @Inject
    private SensorDAO sensorDAO;

    @Inject
    private Datastore db;

    @Inject
    private Logger log;


    public void sendActorMessage(String location, String sensorName, Integer duration) {

        log.info("sendActorMessage: location: {}, actorName: {}, duration: {}", location, sensorName, duration);
        checkParametersOrFail(location, sensorName, duration);

        MqttClient mqttClient = new MqttClient().defaultConnection().
                withTopic(Optional.ofNullable(System.getenv("AMQ_TOPICS")).orElse("actor." + location + "." + sensorName)).
                build();


        WateringData data = new WateringData(new Date(), sensorName, location, duration);
        db.save(data);
        JSONObject json = new JSONObject(data);
        String message = json.toString();
        mqttClient.connectAndPublish(message);
    }

    protected void checkParametersOrFail(String location, String sensorName, Integer duration) {
        if (location == null || location.isEmpty() || sensorName == null || sensorName.isEmpty() || duration == null) {
            Metrics.exceptionsThrown.labels("hogarama_services", "IllegalArgumentException", "ActorServiceImpl.checkParametersOrFail").inc();
            throw new IllegalArgumentException(String.format("Supplied parameters '%s', '%s', '%s' must not be empty or null", location, sensorName, duration));
        }

        String registeredLocation = sensorDAO.getLocationBySensorName(sensorName);
        if (registeredLocation == null) {
            Metrics.exceptionsThrown.labels("hogarama_services", "IllegalArgumentException", "ActorServiceImpl.checkParametersOrFail").inc();
            throw new IllegalArgumentException(sensorName + " is not a registered sensor.");
        }
        if (!registeredLocation.equals(location)) {
            Metrics.exceptionsThrown.labels("hogarama_services", "IllegalArgumentException", "ActorServiceImpl.checkParametersOrFail").inc();
            throw new IllegalArgumentException(String.format("For sensor %s location must be '%s' but was '%s'", sensorName, registeredLocation, location));
        }

    }
}
