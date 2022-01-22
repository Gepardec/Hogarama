package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.RuleDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.event.Event;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RuleServiceTest {

    private static final Long RULE_ID = 3537L;

    @Mock
    private RuleDAO dao;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private RuleService service;
    @SuppressWarnings("unused") @Mock
    Event<LowWaterWateringRule> ruleChanged;

    private Unit unit;
    private Owner owner;

    @BeforeEach
    public void setUp() {
        owner = newOwner();
        Mockito.lenient().when(userContext.getOwner()).thenReturn(owner);
    }

    @Test
    public void createRule_OK() {
        LowWaterWateringRule rule = newRule();

        service.createRule(rule);

        Mockito.verify(dao).save(rule);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void createRule_UnitDoesntBelongToOwner() {
        LowWaterWateringRule rule = newRuleWithNotBelongingUnit();

        Assertions.assertThrows(TechnicalException.class, () -> service.createRule(rule));
    }

    @Test
    public void deleteRule() {
        LowWaterWateringRule rule = newRule();
        Mockito.when(dao.getById(RULE_ID)).thenReturn(Optional.of(rule));

        service.deleteRule(RULE_ID);

        Mockito.verify(dao).delete(rule);
    }

    @Test
    public void updateRule_OK() {
        LowWaterWateringRule rule = newRule();

        service.updateRule(rule);

        Mockito.verify(dao).update(rule);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void updateRule_UnitDoesntBelongToOwner() {
        LowWaterWateringRule rule = newRuleWithNotBelongingUnit();
        Assertions.assertThrows(TechnicalException.class, () -> service.updateRule(rule));
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

    private LowWaterWateringRule newRule() {
        LowWaterWateringRule rule = new LowWaterWateringRule();
        rule.setId(RULE_ID);
        rule.setUnit(unit);
        return rule;
    }

    private LowWaterWateringRule newRuleWithNotBelongingUnit() {
        LowWaterWateringRule rule = new LowWaterWateringRule();
        Unit unit = new Unit();
        unit.setId(-1L);
        rule.setUnit(unit);
        return rule;
    }
}
