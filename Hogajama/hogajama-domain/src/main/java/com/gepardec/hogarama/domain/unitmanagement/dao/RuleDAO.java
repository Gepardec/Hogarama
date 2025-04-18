package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.QLowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Rule entities.
 *
 * <p>
 * This class extends BaseDAO to provide CRUD operations specific to LowWaterWateringRule entities.
 * It also includes additional methods for retrieving rules based on specific criteria
 * such as user ownership and associated sensor.
 * </p>
 *
 * <p>
 * Rules define the automation logic that ties sensors and actors together, specifying conditions
 * under which actors should be triggered based on sensor readings.
 * </p>
 *
 * <p>
 * The class uses QueryDSL for type-safe queries.
 * </p>
 */
@Dependent
public class RuleDAO extends BaseDAO<LowWaterWateringRule> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<LowWaterWateringRule> getEntityClass() {
        return LowWaterWateringRule.class;
    }

    /**
     * Retrieves all rules owned by a specific user.
     *
     * <p>
     * This method finds all rules associated with units that belong to the specified user.
     * </p>
     *
     * @param user The user whose rules should be retrieved
     * @return A list of LowWaterWateringRule entities owned by the user
     */
    public List<LowWaterWateringRule> getAllRulesForUser(User user) {
        JPAQuery<LowWaterWateringRule> query = new JPAQuery<>(entityManager);
        QLowWaterWateringRule rule = QLowWaterWateringRule.lowWaterWateringRule;
        return query.select(rule).from(rule).where(rule.unit.user.id.eq(user.getId())).fetch();
    }

    /**
     * Finds a rule associated with a specific sensor.
     *
     * <p>
     * This method retrieves the watering rule that is configured for a particular sensor,
     * identified by its device ID.
     * </p>
     *
     * @param sensorName The device identifier of the sensor
     * @return An Optional containing the rule if found, or empty if not found
     */
    public Optional<LowWaterWateringRule> getBySensor(String sensorName) {
        JPAQuery<LowWaterWateringRule> query = new JPAQuery<>(entityManager);
        QLowWaterWateringRule rule = QLowWaterWateringRule.lowWaterWateringRule;
        return Optional.ofNullable(query.select(rule).from(rule).where(rule.sensor.deviceId.eq(sensorName)).fetchOne());
    }
}
