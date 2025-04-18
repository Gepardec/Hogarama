package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.QUnit;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.Dependent;

import java.util.List;

/**
 * Data Access Object for Unit entities.
 *
 * <p>
 * This class extends BaseDAO to provide CRUD operations specific to Unit entities.
 * It also includes additional methods for retrieving units based on specific criteria
 * such as user ownership.
 * </p>
 *
 * <p>
 * Units represent logical groupings of sensors and actors, typically corresponding to
 * a specific location or plant that is being monitored and controlled.
 * </p>
 *
 * <p>
 * The class uses QueryDSL for type-safe queries.
 * </p>
 */
@Dependent
public class UnitDAO extends BaseDAO<Unit> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Unit> getEntityClass() {
        return Unit.class;
    }

    /**
     * Retrieves all units owned by a specific user.
     *
     * @param userId The unique identifier of the user whose units should be retrieved
     * @return A list of Unit entities owned by the user
     */
    public List<Unit> getUnitsForUser(Long userId) {
        JPAQuery<Sensor> query = new JPAQuery<>(entityManager);
        QUnit unit = QUnit.unit;
        return query.select(unit).from(unit).where(unit.unit.user.id.eq(userId)).fetch();
    }
}
