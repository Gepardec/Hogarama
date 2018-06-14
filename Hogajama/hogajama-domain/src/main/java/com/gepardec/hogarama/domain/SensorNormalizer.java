package com.gepardec.hogarama.domain;

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
}
