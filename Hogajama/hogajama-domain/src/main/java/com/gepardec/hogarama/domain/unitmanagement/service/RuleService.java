package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.RuleDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class RuleService {

    @Inject
    private RuleDAO dao;

    @Inject
    private UserContext userContext;

    @Inject
    Event<LowWaterWateringRule> ruleChanged;

    public void createRule(LowWaterWateringRule rule) {
        rule.verifyIsOwned(userContext.getOwner());
        dao.save(rule);
    }

    public void deleteRule(Long ruleId) {
        LowWaterWateringRule rule = this.dao.getById(ruleId)
                .orElseThrow(() -> new NotFoundException(String.format("Rule with id [%d] not found", ruleId)));

        ruleChanged.fire(rule);
        dao.delete(rule);
    }

    public void updateRule(LowWaterWateringRule rule) {
        rule.verifyIsOwned(userContext.getOwner());
        ruleChanged.fire(rule);
        dao.update(rule);
    }

    public List<LowWaterWateringRule> getAllRulesForOwner() {
        return dao.getAllRulesForOwner(userContext.getOwner());
    }

}
