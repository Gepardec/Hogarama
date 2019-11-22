package com.gepardec.hogarama.domain.cache;

import com.gepardec.hogarama.domain.entity.QSensorType;
import com.gepardec.hogarama.domain.entity.SensorType;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(SensorTypeCache.class);

    @PersistenceContext
    protected EntityManager entityManager;

    private Map<Long, SensorType> sensorTypeMap;

    @PostConstruct
    public void init() {
        loadCache();
    }

    public Optional<SensorType> byId(Long id) {
        SensorType type = sensorTypeMap.get(id);
        // refresh cache
        if (type == null) {
            loadCache();
            LOG.warn("No sensor with id {} found - Cache refreshed.", id);
        }
        return Optional.ofNullable(sensorTypeMap.get(id));
    }

    private void loadCache() {
        JPAQuery<SensorType> query = new JPAQuery<>(entityManager);
        QSensorType sensorType = QSensorType.sensorType;
        sensorTypeMap = query.select(sensorType).from(sensorType).fetch().stream()
                .collect(Collectors.toMap(
                        SensorType::getId,
                        Function.identity()));
    }
}
