package com.gepardec.hogarama.domain.unitmanagement.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;

@ApplicationScoped
public class SensorCache {

    private static final Logger LOG = LoggerFactory.getLogger(SensorCache.class);

    @Inject
    private SensorDAO dao;
    
    private Map<String, Sensor> cache = new HashMap<>();

    public void invalidateCache(@Observes Sensor sensor) {
        LOG.info("Invalidate sensor cache.");
        cache = new HashMap<>();
    }
    
    public Optional<Sensor> getByDeviceId(String deviceId) {
        Optional<Sensor> sensor = getCachedSensor(deviceId);
        if ( sensor.isPresent()) {
            return sensor;
        }
        return loadSensor(deviceId);
    }

    private synchronized Optional<Sensor> loadSensor(String deviceId) {
        Optional<Sensor> sensor = getCachedSensor(deviceId);
        if (sensor.isPresent()) {
            return sensor;
        }

        LOG.info("Sensor not found in cache, read from database with deviceId: " + deviceId);
        sensor = dao.getByDeviceId(deviceId);
        if (sensor.isPresent()) {
            touchSensorTypeAndUnit(sensor);
            cache.put(deviceId, sensor.get());
        }
        return sensor;
    }

    private Optional<Sensor> getCachedSensor(String deviceId) {
        return Optional.ofNullable(cache.get(deviceId));
    }

    private void touchSensorTypeAndUnit(Optional<Sensor> sensor) {
        SensorType type = sensor.get().getSensorType();
        String unitName = sensor.get().getUnit().getName();
        LOG.info("Sensor {} found. SensorType: {}, UnitName: {}", 
                sensor.get().getName(), type.getName(), unitName);
    }
}
