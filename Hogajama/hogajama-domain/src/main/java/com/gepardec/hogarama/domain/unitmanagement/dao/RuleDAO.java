package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.Rule;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.QRule;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.List;
import java.util.Optional;

@Dependent
public class RuleDAO extends BaseDAO<Rule> {

    @Override
    public Class<Rule> getEntityClass() {
        return Rule.class;
    }

    public List<Rule> getAllRulesForOwner(Owner owner) {
        JPAQuery<Rule> query = new JPAQuery<>(entityManager);
        QRule rule = QRule.rule;
        return query.select(rule).from(rule).where(rule.unit.owner.id.eq(owner.getId())).fetch();
    }

    public Optional<Rule> getBySensor(String sensorName) {
        JPAQuery<Rule> query = new JPAQuery<>(entityManager);
        QRule rule = QRule.rule;
        return Optional.ofNullable(query.select(rule).from(rule).where(rule.sensor.deviceId.eq(sensorName)).fetchOne());
    }
}
