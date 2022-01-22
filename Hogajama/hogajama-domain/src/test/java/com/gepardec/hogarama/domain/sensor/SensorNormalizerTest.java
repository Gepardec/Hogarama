package com.gepardec.hogarama.domain.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;

public class SensorNormalizerTest {

	private static final String LINEAR1024 = "LINEAR1024";
	private static final double PRECISION = 0.01;
	private static final String DEVICE_ID = "My Plant";

	@SuppressWarnings("FieldCanBeLocal")
	private SensorCache sensorCache;
	private SensorNormalizer sn;

	private static List<Arguments> data() {
		return Arrays.asList(
				Arguments.of(0.56, "UnknownSensorType", 56.0),
				Arguments.of(0.0, LINEAR1024, 0.0),
				Arguments.of(1.0, LINEAR1024, 1024.0),
				Arguments.of(0.5, LINEAR1024, 512.0),
				Arguments.of(0.0, "INVERSE_LINEAR1024", 1024.0),
				Arguments.of(1.0, "INVERSE_LINEAR1024", 0.0)
		);
	}

	@BeforeEach
	public void setUpMethod() {
		sn = new SensorNormalizer();
		sensorCache = mock(SensorCache.class);
		Mockito.when(sensorCache.getByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());
		sn.setSensorCache(sensorCache);
	}

	@ParameterizedTest
	@MethodSource("data")
	public void testNormalize(Double expectedValue, String type, Double value) {
		assertEquals(expectedValue, normalized(type, value), PRECISION);
	}

	private double normalized(String type, Double value) {
		return sn.normalize(createSensorData(type, value)).getValue();
	}

	private static SensorData createSensorData(String type, Double value) {
		return new SensorData("1", new Date(), "My Plant", type, value, "Vienna", "1.0");
	}
	
	static void checkNormalised(List<SensorData> sensorData) {
		int index = 0;
		for (Arguments arguments : data()) {
			assertEquals(arguments.get()[0], sensorData.get(index).getValue(), "Index " + index);
			index++;
		}
	}

	static List<SensorData> getDataList() {
		return data().stream().map(arguments ->
			SensorNormalizerTest.createSensorData((String)arguments.get()[1], (double)arguments.get()[2])
		).collect(Collectors.toList());
	}

}
