package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.RuleDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Rule;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RuleServiceTest {

    private static final Long RULE_ID = 3537L;

    @Mock
    private RuleDAO dao;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private RuleService service;
    @Mock
    Event<Rule> ruleChanged;

    private Unit unit;
    private Owner owner;

    @Before
    public void setUp() {
        owner = newOwner();
        Mockito.when(userContext.getOwner()).thenReturn(owner);
    }

    @Test
    public void createRule_OK() {
        Rule rule = newRule();

        service.createRule(rule);

        Mockito.verify(dao).save(rule);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test(expected = TechnicalException.class)
    public void createRule_UnitDoesntBelongToOwner() {
        Rule rule = newRuleWithNotBelongingUnit();

        service.createRule(rule);
    }

    @Test
    public void deleteRule() {
        Rule rule = newRule();
        Mockito.when(dao.getById(RULE_ID)).thenReturn(Optional.of(rule));

        service.deleteRule(RULE_ID);

        Mockito.verify(dao).delete(rule);
    }

    @Test
    public void updateRule_OK() {
        Rule rule = newRule();

        service.updateRule(rule);

        Mockito.verify(dao).update(rule);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test(expected = TechnicalException.class)
    public void updateRule_UnitDoesntBelongToOwner() {
        Rule rule = newRuleWithNotBelongingUnit();

        service.updateRule(rule);
    }

    @Test
    public void getAllRulesForOwner() {
        service.getAllRulesForOwner();

        Mockito.verify(dao).getAllRulesForOwner(owner);
    }

    private Owner newOwner() {
        Owner owner = new Owner();
        this.unit = new Unit();
        unit.setId(1337L);
        owner.setUnitList(Collections.singletonList(unit));
        return owner;
    }

    private Rule newRule() {
        Rule rule = new Rule();
        rule.setId(RULE_ID);
        rule.setUnit(unit);
        return rule;
    }

    private Rule newRuleWithNotBelongingUnit() {
        Rule rule = new Rule();
        Unit unit = new Unit();
        unit.setId(-1L);
        rule.setUnit(unit);
        return rule;
    }
}
