package com.gepardec.hogarama.domain.sensor;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;

@ApplicationScoped
public class SensorNormalizer {
    private static final Logger LOG = LoggerFactory.getLogger(SensorNormalizer.class);

    @Inject
    private SensorCache sensors;
    
	public SensorData normalize(SensorData data) {
			data.setValue(
					mappingTypeOf(data)
					.map(data.getValue())
			);
			return data;
	}

	private MappingType mappingTypeOf(SensorData data) {
	    Optional<Sensor> sensor = sensors.getByDeviceId(data.getSensorName());
	    if (sensor.isPresent()) {
	        return sensor.get().getSensorType().getMappingType();
	    }
	    LOG.warn("Sensor {} not found in database. We use MappingType from data. "
	    + "You should create the sensor in database!", data.getSensorName());
		return SensorType.getMappingType(data.getType());
	}

	public List<SensorData> normalize(List<SensorData> data) {
		for (SensorData sensorData : data) {
			normalize(sensorData);
		}
		return data;
	}

    public void setSensorCache(SensorCache sensorCache) {
        this.sensors = sensorCache;
        
    }
}
