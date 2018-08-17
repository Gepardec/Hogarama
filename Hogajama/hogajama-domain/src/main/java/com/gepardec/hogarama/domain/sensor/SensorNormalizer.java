package com.gepardec.hogarama.domain.sensor;

import java.util.List;

public class SensorNormalizer {
	
	public SensorData normalize(SensorData data) {
			data.setValue(
					mappingTypeOf(data)
					.map(data.getValue())
			);
			return data;
	}

	private MappingType mappingTypeOf(SensorData data) {
		return SensorType.getMappingType(data.getType());
	}

	public List<SensorData> normalize(List<SensorData> data) {
		for (SensorData sensorData : data) {
			normalize(sensorData);
		}
		return data;
	}
}
