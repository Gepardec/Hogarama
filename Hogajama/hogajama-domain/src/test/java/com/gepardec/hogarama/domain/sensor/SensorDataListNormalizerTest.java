package com.gepardec.hogarama.domain.sensor;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;

public class SensorDataListNormalizerTest {

	private SensorNormalizer sn;

	@Before
	public void setUpMethod() throws Exception {
		sn = new SensorNormalizer();
	}

	@Test
	public void emptyListReturnsEmptyList() {
		List<SensorData> data = sn.normalize(Collections.emptyList());
		assertTrue(data.isEmpty());
	}

	@Test
	public void testFullList() throws Exception {
		SensorNormalizerTest.checkNormalised(
				sn.normalize(SensorNormalizerTest.getDataList()));
	}
}
