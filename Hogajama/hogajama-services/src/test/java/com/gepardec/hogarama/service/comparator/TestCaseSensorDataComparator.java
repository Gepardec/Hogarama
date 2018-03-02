package com.gepardec.hogarama.service.comparator;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.gepardec.hogarama.domain.SensorData;
import com.gepardec.hogarama.util.DummyDataUtil;

public class TestCaseSensorDataComparator {

	private List<SensorData> list = DummyDataUtil.getDummySensorData(2, 100, true);
	private SensorDataComparator sensorDataComparator = new SensorDataComparator();
	
	@Test
	public void testSensorComparator() {
		List<SensorData> orderedList = list.stream().sorted(sensorDataComparator).collect(Collectors.toList());
		assertTrue(!orderedList.isEmpty());
		
		SensorData prevSensorData = null;
		for(SensorData sensorData : orderedList) {
			if(prevSensorData == null) {
				prevSensorData = sensorData;
			} else {
				if(prevSensorData.getTime().getTime() > sensorData.getTime().getTime()) {
					assertTrue("The order of the list is not correct", false);
				}
				prevSensorData = sensorData;
			}
		}
		assertTrue(true);
	}
	
}
