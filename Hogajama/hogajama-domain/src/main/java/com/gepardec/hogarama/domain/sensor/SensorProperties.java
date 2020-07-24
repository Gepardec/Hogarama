package com.gepardec.hogarama.domain.sensor;

import java.util.Optional;
import java.util.function.IntPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;

public class SensorProperties {

    private static final Logger LOG = LoggerFactory.getLogger(SensorProperties.class);

    private SensorData data;
    private String deviceId;
    Optional<Sensor> sensor;

    public SensorProperties(SensorData data, SensorCache dao) {
        this.data = data;
        deviceId = data.getSensorName();
        sensor = dao.getByDeviceId(deviceId);
        if (! sensor.isPresent()) {
            LOG.warn("Sensor {} not found in database. We use MappingType from data. "
                    + "You should create the sensor in the database!", data.getSensorName());
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSensorName() {
        return sensor.isPresent() ? sensor.get().getName() : deviceId;
    }

    public String getUnitName() {
        return sensor.isPresent() ? sensor.get().getUnit().getName() : data.getLocation();
    }

    public MappingType getMappingType() {
        return sensor.isPresent() ?
            sensor.get().getSensorType().getMappingType() :
            SensorType.getMappingType(data.getType());
    }

}
