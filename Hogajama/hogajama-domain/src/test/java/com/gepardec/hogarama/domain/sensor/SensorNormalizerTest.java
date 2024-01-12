package com.gepardec.hogarama.domain.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class SensorNormalizerTest {

	private static final String LINEAR1024 = "LINEAR1024";
	private static final double PRECISION = 0.01;
	private SensorNormalizer sn;
	private Double expectedValue;
	private String type;
	private Double value;
    private static final String DEVICE_ID = "My Plant";

	SensorCache sensorCache;

	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {

				{ 0.56, "UnknownSensorType", 56.0 },
				{ 0.0, LINEAR1024, 0.0 },
				{ 1.0, LINEAR1024, 1024.0 },
				{ 0.5, LINEAR1024, 512.0 },
				{ 0.0, "INVERSE_LINEAR1024", 1024.0 },
				{ 1.0, "INVERSE_LINEAR1024", 0.0 }

		};
		return Arrays.asList(data);
	}

    public void initSensorNormalizerTest(Double expectedValue, String type, Double value) {
		super();
		this.expectedValue = expectedValue;
		this.type = type;
		this.value = value;
	}

	@BeforeEach
	public void setUpMethod() throws Exception {
		sn = new SensorNormalizer();
		sensorCache = mock(SensorCache.class);
		Mockito.when(sensorCache.getByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());
		sn.setSensorCache(sensorCache);
	}


    @MethodSource("data")
    @ParameterizedTest
    public void testNormalize(Double expectedValue, String type, Double value) throws Exception {
        initSensorNormalizerTest(expectedValue, type, value);
		assertEquals(expectedValue, normalized(type, value), PRECISION);
	}

	private double normalized(String type, Double value) {
		return sn.normalize(createSensorData(type, value)).getValue();
	}

	static public SensorData createSensorData(String type, Double value) {
		return new SensorData("1", new Date(), "My Plant", type, value, "Vienna", "1.0");
	}
	
	static public void checkNormalised(List<SensorData> sensorData) {
		int index = 0;
		for (Object[] objects : data()) {
			assertEquals(objects[0], sensorData.get(index).getValue(), "Index " + index);
			index++;
		}
	}

	static public List<SensorData> getDataList() {
		List<SensorData> sensorData = new ArrayList<SensorData>();
		for (Object[] objects : data()) {
			sensorData.add(SensorNormalizerTest.createSensorData((String)objects[1], (double)objects[2]));
		}
		return sensorData;
	}

}
