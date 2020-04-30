package com.gepardec.hogarama.domain.unitmanagement.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.unitmanagement.entity.QSensorType;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;
import com.querydsl.jpa.impl.JPAQuery;

@ApplicationScoped
public class SensorTypeCache {

    private static final Logger LOG = LoggerFactory.getLogger(SensorTypeCache.class);

    @PersistenceContext
    protected EntityManager entityManager;

    private Map<Long, SensorType> sensorTypeMap = new HashMap<>();

    public Optional<SensorType> byId(Long id) {
        Optional<SensorType> type = getCachedType(id);
        if (type.isPresent() ) {
            return type;
        }
        return loadType(id);
     }

    private Optional<SensorType> getCachedType(Long id) {
        return Optional.ofNullable(sensorTypeMap.get(id));
    }

    private synchronized Optional<SensorType> loadType(Long id) {
        Optional<SensorType> type = getCachedType(id);
        if (type.isPresent() ) {
            return type;
        }

        LOG.warn("No sensor with id {} found - refresh cache.", id);
        JPAQuery<SensorType> query = new JPAQuery<>(entityManager);
        QSensorType sensorType = QSensorType.sensorType;
        sensorTypeMap = query.select(sensorType).from(sensorType).fetch().stream()
                .collect(Collectors.toMap(
                        SensorType::getId,
                        Function.identity()));
        return getCachedType(id);
    }
}
