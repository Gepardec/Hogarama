package com.gepardec.hogarama.domain.sensor;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;

@ApplicationScoped
public class SensorNormalizer {

    @Inject
    private SensorCache sensorDao;
    
	public SensorData normalize(SensorData data) {
			data.setValue(
					mappingTypeOf(data)
					.map(data.getValue())
			);
			return data;
	}

	private MappingType mappingTypeOf(SensorData data) {	    
        return new SensorProperties(data, sensorDao).getMappingType();
	}

	public List<SensorData> normalize(List<SensorData> data) {
		for (SensorData sensorData : data) {
			normalize(sensorData);
		}
		return data;
	}

    public void setSensorCache(SensorCache sensorCache) {
        this.sensorDao = sensorCache;
        
    }
}
