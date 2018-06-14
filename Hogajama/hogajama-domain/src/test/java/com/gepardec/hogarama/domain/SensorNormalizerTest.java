package com.gepardec.hogarama.domain;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SensorNormalizerTest {

	private static final String LINEAR1024 = "LINEAR1024";
	private static final double PRECISION = 0.01;
	private SensorNormalizer sn;
	private Double expectedValue;
	private String type;
	private Double value;
	

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {

				{ 56.0, "UnknownSensorType", 56.0 },
				{ 0.0, LINEAR1024, 0.0 },
				{ 1.0, LINEAR1024, 1024.0 },
				{ 0.5, LINEAR1024, 512.0 },
				{ 0.0, "INVERSE_LINEAR1024", 1024.0 },
				{ 1.0, "INVERSE_LINEAR1024", 0.0 }

		};
		return Arrays.asList(data);
	}

	public SensorNormalizerTest(Double expectedValue, String type, Double value) {
		super();
		this.expectedValue = expectedValue;
		this.type = type;
		this.value = value;
	}

	@Before
	public void setUpMethod() throws Exception {
		sn = new SensorNormalizer();
	}
	
	
	@Test
	public void testNormalize() throws Exception {
		assertEquals(expectedValue, normalized(type, value), PRECISION);
	}

	private double normalized(String type, Double value) {
		return sn.normalize(createSensorData(type, value)).getValue();
	}

	private SensorData createSensorData(String type, Double value) {
		return new SensorData("1", new Date(), "My Plant", type, value, "Vienna", "1.0");
	}
}
