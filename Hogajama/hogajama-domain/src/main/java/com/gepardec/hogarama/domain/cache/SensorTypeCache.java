package com.gepardec.hogarama.domain.cache;

import com.gepardec.hogarama.domain.entity.QSensorType;
import com.gepardec.hogarama.domain.entity.SensorType;
import com.querydsl.jpa.impl.JPAQuery;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class SensorTypeCache {

    @PersistenceContext
    protected EntityManager entityManager;

    private Map<Long, SensorType> sensorTypeMap;

    @PostConstruct
    public void init() {
        JPAQuery<SensorType> query = new JPAQuery<>(entityManager);
        QSensorType sensorType = QSensorType.sensorType;
        sensorTypeMap = query.select(sensorType).from(sensorType).fetch().stream()
                .collect(Collectors.toMap(
                        SensorType::getId,
                        Function.identity()));
    }

    public Optional<SensorType> byId(Long id) {
        // TODO refresh cache if there is no senorType with given id found
        return Optional.ofNullable(sensorTypeMap.get(id));
    }
}
