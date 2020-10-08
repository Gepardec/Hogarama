package com.gepardec.hogarama.service.dao;

import javax.annotation.Priority;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import com.gepardec.hogarama.annotations.ConfiguredDAO;
import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.domain.watering.WateringRuleDAO;

//@Priority(Interceptor.Priority.APPLICATION)
public class WateringSetup {

    @Inject @MongoDAO
    private MongoWateringRuleDAO mongoDAO;

    @Produces @ConfiguredDAO
    WateringRuleDAO createWateringRuleDao() {
        if ( System.getProperty("hogarama.rules.storage").contentEquals("postgres")) {
            return null;
        }
        return mongoDAO;
    }
}
