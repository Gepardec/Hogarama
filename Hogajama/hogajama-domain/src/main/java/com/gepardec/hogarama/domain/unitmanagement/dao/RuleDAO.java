package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.QLowWaterWateringRule;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.List;
import java.util.Optional;

@Dependent
public class RuleDAO extends BaseDAO<LowWaterWateringRule> {

    @Override
    public Class<LowWaterWateringRule> getEntityClass() {
        return LowWaterWateringRule.class;
    }

    public List<LowWaterWateringRule> getAllRulesForUser(User user) {
        JPAQuery<LowWaterWateringRule> query = new JPAQuery<>(entityManager);
        QLowWaterWateringRule rule = QLowWaterWateringRule.lowWaterWateringRule;
        return query.select(rule).from(rule).where(rule.unit.user.id.eq(user.getId())).fetch();
    }

    public Optional<LowWaterWateringRule> getBySensor(String sensorName) {
        JPAQuery<LowWaterWateringRule> query = new JPAQuery<>(entityManager);
        QLowWaterWateringRule rule = QLowWaterWateringRule.lowWaterWateringRule;
        return Optional.ofNullable(query.select(rule).from(rule).where(rule.sensor.deviceId.eq(sensorName)).fetchOne());
    }
}
