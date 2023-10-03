package com.gepardec.hogarama.service.dao;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.annotations.PostgresDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.PostgresWateringRuleDAO;
import com.gepardec.hogarama.domain.watering.WateringRuleDAO;

public class WateringSetup {

    private static final Logger LOG = LoggerFactory.getLogger(WateringSetup.class);

    @Inject @MongoDAO
    private MongoWateringRuleDAO mongoDAO;

    @Inject @PostgresDAO
    private PostgresWateringRuleDAO postgresDAO;

    @Produces
    WateringRuleDAO createWateringRuleDao() {
        switch (System.getProperty("hogarama.rules.storage", "postgres")) {
        case "postgres":
            LOG.debug("Produce PostgresWateringRuleDAO");
            return postgresDAO;
        case "mongo":
            LOG.debug("Produce MongoWateringRuleDAO");
            return mongoDAO;
        default:
            throw new RuntimeException(System.getProperty("hogarama.rules.storage", "postgres")
                    + " is not valid for the System Property hogarama.rules.storage."
                    + " Allowed values are 'postgres' or 'mongo'.");
        }
    }
}
