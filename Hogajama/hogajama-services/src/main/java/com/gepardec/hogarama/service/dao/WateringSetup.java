package com.gepardec.hogarama.service.dao;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.domain.watering.WateringRuleDAO;

public class WateringSetup {

    @Inject @MongoDAO
    private MongoWateringRuleDAO mongoDAO;

    @Produces
    WateringRuleDAO createWateringRuleDao() {
        if ( System.getProperty("hogarama.rules.storage").contentEquals("postgres")) {
            return null;
        }
        return mongoDAO;
    }
}
