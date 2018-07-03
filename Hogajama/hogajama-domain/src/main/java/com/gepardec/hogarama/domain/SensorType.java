package com.gepardec.hogarama.domain;

import java.util.HashMap;

/**
 * In this class all sensor-types are defined. 
 * You have to define how a sensors values values are mapped to an [0..1] interval by defining a {@link MappingType} for each sensor type
 * If a unknown sensor type is sent to the system it is not mapped an should sent values within [0..1]. 
 * If it doesn't the result is undefined.
 *
 */
public class SensorType {

	@SuppressWarnings("serial")
	private static final HashMap<String, MappingType> SENSORS = new HashMap<String, MappingType>() {{
	    put("LINEAR1024", MappingType.LINEAR1024);
	    put("INVERSE_LINEAR1024", MappingType.INVERSE_LINEAR1024);
	    put("Chinese Water Sensor", MappingType.INVERSE_LINEAR1024);
	}};

	public static MappingType getMappingType(String type) {
		return SENSORS.getOrDefault(type, MappingType.LINEAR100);
	}
}
