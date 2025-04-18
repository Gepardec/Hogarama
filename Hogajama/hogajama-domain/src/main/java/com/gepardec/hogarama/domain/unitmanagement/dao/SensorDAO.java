package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.QSensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Sensor entities.
 *
 * <p>
 * This class extends BaseDAO to provide CRUD operations specific to Sensor entities.
 * It also includes additional methods for retrieving sensors based on specific criteria
 * such as user ownership and device identifier.
 * </p>
 *
 * <p>
 * The class uses QueryDSL for type-safe queries.
 * </p>
 */
@Dependent
public class SensorDAO extends BaseDAO<Sensor> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Sensor> getEntityClass() {
        return Sensor.class;
    }

    /**
     * Retrieves all sensors owned by a specific user.
     *
     * <p>
     * This method finds all sensors associated with units that belong to the specified user.
     * </p>
     *
     * @param user The user whose sensors should be retrieved
     * @return A list of Sensor entities owned by the user
     */
    public List<Sensor> getAllSensorsForUser(User user) {
        JPAQuery<Sensor> query = new JPAQuery<>(entityManager);
        QSensor sensor = QSensor.sensor;
        return query.select(sensor).from(sensor).where(sensor.unit.user.id.eq(user.getId())).fetch();
    }

    /**
     * Finds a sensor by its device identifier.
     *
     * <p>
     * Device identifier is a unique string used to identify physical sensor devices
     * in the system.
     * </p>
     *
     * @param deviceId The device identifier to search for
     * @return An Optional containing the Sensor if found, or empty if not found
     */
    public Optional<Sensor> getByDeviceId(String deviceId) {
        JPAQuery<Sensor> query = new JPAQuery<>(entityManager);
        QSensor sensor = QSensor.sensor;
        return Optional.ofNullable(query.select(sensor).from(sensor).where(sensor.deviceId.eq(deviceId)).fetchOne());
    }
}
