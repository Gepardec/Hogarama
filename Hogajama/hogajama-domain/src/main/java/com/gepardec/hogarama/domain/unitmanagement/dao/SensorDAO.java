package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.QSensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent
public class SensorDAO extends BaseDAO<Sensor> {

    @Override
    public Class<Sensor> getEntityClass() {
        return Sensor.class;
    }

    public List<Sensor> getAllSensorForOwner(Owner owner) {
        JPAQuery<Sensor> query = new JPAQuery<>(entityManager);
        QSensor sensor = QSensor.sensor;
        return query.select(sensor).from(sensor).where(sensor.unit.owner.id.eq(owner.getId())).fetch();
    }
}
