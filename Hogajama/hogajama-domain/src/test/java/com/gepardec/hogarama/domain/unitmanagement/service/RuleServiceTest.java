package com.gepardec.hogarama.domain.unitmanagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.RuleDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.enterprise.event.Event;
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
    @Mock
    Event<LowWaterWateringRule> ruleChanged;

    private Unit unit;
    private User user;

    @BeforeEach
    public void setUp() {
        user = newUser();
        Mockito.when(userContext.getUser()).thenReturn(user);
    }

    @Test
    public void createRule_OK() {
        LowWaterWateringRule rule = newRule();

        service.createRule(rule);

        Mockito.verify(dao).save(rule);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void createRule_UnitDoesntBelongToUser() {
        assertThrows(TechnicalException.class, () -> {
            LowWaterWateringRule rule = newRuleWithNotBelongingUnit();

            service.createRule(rule);
        });
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
    public void updateRule_UnitDoesntBelongToUser() {
        assertThrows(TechnicalException.class, () -> {
            LowWaterWateringRule rule = newRuleWithNotBelongingUnit();

            service.updateRule(rule);
        });
    }

    @Test
    public void getAllRulesForUser() {
        service.getAllRulesForUser();

        Mockito.verify(dao).getAllRulesForUser(user);
    }

    private User newUser() {
        User user = new User();
        user.setId(-1L);
        this.unit = new Unit();
        unit.setId(1337L);
        unit.setUser(user);
        user.setUnitList(Collections.singletonList(unit));
        return user;
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
