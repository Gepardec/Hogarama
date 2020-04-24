package com.gepardec.hogarama.domain.unitmanagement.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
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
    
    private Map<String, Optional<Sensor>> cache = new HashMap<>();


    public Optional<Sensor> getByDeviceId(String deviceId) {
        Optional<Sensor> sensor = cache.get(deviceId);
        if ( null == cache.get(deviceId) || !sensor.isPresent()) {
            LOG.info("Sensor not found in cache, read from database with deviceId: " + deviceId);
            sensor = dao.getByDeviceId(deviceId);
            if ( sensor.isPresent() ) {
                // touch SensorType
                SensorType type = sensor.get().getSensorType();
                LOG.info("Sensor {} found in cache. SensorType: {}", sensor.get().getName(), type.getName());
            }
            cache.put(deviceId, sensor);
        }
        return sensor;
    }
}
