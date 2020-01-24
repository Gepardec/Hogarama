package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.entity.QSensor;
import com.gepardec.hogarama.domain.entity.Sensor;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent
public class SensorDao extends BaseDao<Sensor> {

    @Override
    public Class<Sensor> getEntityClass() {
        return Sensor.class;
    }

    public List<Sensor> getAllSensorForOwner(Long ownerId) {
        JPAQuery<Sensor> query = new JPAQuery<>(entityManager);
        QSensor sensor = QSensor.sensor;
        return query.select(sensor).from(sensor).where(sensor.unit.owner.id.eq(ownerId)).fetch();
    }
}
