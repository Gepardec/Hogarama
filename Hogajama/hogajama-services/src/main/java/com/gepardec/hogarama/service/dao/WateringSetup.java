package com.gepardec.hogarama.service.dao;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.annotations.DummyDAO;
import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.annotations.PostgresDAO;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.PostgresWateringRuleDAO;
import com.gepardec.hogarama.domain.watering.WateringRuleDAO;

public class WateringSetup {

    private static final Logger LOG = LoggerFactory.getLogger(WateringSetup.class);

    @Inject @MongoDAO
    private MongoWateringRuleDAO mongoDAO;

    @Inject @PostgresDAO
    private PostgresWateringRuleDAO postgresDAO;

    @Inject @MongoDAO
    private SensorDataDAO  mongoSensorDataDAO;

    @Inject @DummyDAO
    private SensorDataDAO  dummySensorDataDAO;

    @Produces
    WateringRuleDAO createWateringRuleDao() {
        switch (System.getProperty("hogarama.rules.storage", "postgres")) {
        case "postgres":
            LOG.info("Produce PostgresWateringRuleDAO");
            return postgresDAO;
        case "mongo":
            LOG.info("Produce MongoWateringRuleDAO");
            return mongoDAO;
        default:
            throw new RuntimeException(System.getProperty("hogarama.rules.storage", "postgres")
                    + " is not valid for the System Property hogarama.rules.storage."
                    + " Allowed values are 'postgres' or 'mongo'.");
        }
    }
    
    @Produces
    SensorDataDAO createSensorDataDAO() {
        switch (System.getProperty("hogarama.sensordata.storage", "mongo")) {
        case "mongo":
            LOG.info("Produce mongoSensorDataDAO");
            return mongoSensorDataDAO;
        case "dummy":
            LOG.info("Produce dummySensorDataDAO");
            return dummySensorDataDAO;
        default:
            throw new RuntimeException(System.getProperty("hogarama.sensordata.storage", "mongo")
                    + " is not valid for the System Property hogarama.sensordata.storage."
                    + " Allowed values are 'mongo' or 'dummy'.");
        }
    }
}
