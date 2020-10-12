package com.gepardec.hogarama.service.dao;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.annotations.PostgresDAO;
import com.gepardec.hogarama.domain.watering.WateringRuleDAO;

//@Priority(Interceptor.Priority.APPLICATION)
public class WateringSetup {

    private static final Logger LOG = LoggerFactory.getLogger(WateringSetup.class);

    @Inject @MongoDAO
    private MongoWateringRuleDAO mongoDAO;

    @Inject @PostgresDAO
    private PostgresWateringRuleDAO postgresDAO;

    @Produces
    WateringRuleDAO createWateringRuleDao() {
        if ( System.getProperty("hogarama.rules.storage").contentEquals("postgres")) {
            LOG.debug("Produce PostgresWateringRuleDAO");
            return postgresDAO;
        }
        LOG.debug("Produce MongoWateringRuleDAO");
        return mongoDAO;
    }
}
