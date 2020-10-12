package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.RuleDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Rule;

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
    Event<Rule> ruleChanged;

    public void createRule(Rule rule) {
        rule.verifyIsOwned(userContext.getOwner());
        dao.save(rule);
    }

    public void deleteRule(Long ruleId) {
        Rule rule = this.dao.getById(ruleId)
                .orElseThrow(() -> new NotFoundException(String.format("Rule with id [%d] not found", ruleId)));

        ruleChanged.fire(rule);
        dao.delete(rule);
    }

    public void updateRule(Rule rule) {
        rule.verifyIsOwned(userContext.getOwner());
        ruleChanged.fire(rule);
        dao.update(rule);
    }

    public List<Rule> getAllRulesForOwner() {
        return dao.getAllRulesForOwner(userContext.getOwner());
    }

}
