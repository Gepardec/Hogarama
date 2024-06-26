package com.gepardec.hogarama.domain.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.gepardec.hogarama.dao.dummydata.DummyData;
import com.gepardec.hogarama.dao.dummydata.SensorDataPredicate;

public class TestCaseSensorDataPredicate {

	private List<SensorData> list = DummyData.getDummySensorData(2, 100, true);

	@Test
	public void testSensorDataPredicateBadName() {
		List<SensorData> result = list.stream()
									  .filter(new SensorDataPredicate("NOTIN123", null, null))
									  .collect(Collectors.toList());
		assertEquals(0, result.size());
	}

	@Test
	public void testSensorDataPredicateNullName() {
		List<SensorData> result = list.stream()
									  .filter(new SensorDataPredicate(null, null, null))
									  .collect(Collectors.toList());
		assertEquals(0, result.size());
	}

	@Test
	public void testSensorDataPredicateWithName() {
		List<SensorData> result = list.stream()
									  .filter(new SensorDataPredicate(DummyData.SENSOR_NAME + "0", null, null))
									  .collect(Collectors.toList());
		assertTrue(result.size() > 0);
	}

	@Test
	public void testSensorDataPredicateWithNameAndFrom1() {
		Calendar calendar = new GregorianCalendar(1990, 01, 01);
		List<SensorData> result = list.stream()
				  					  .filter(new SensorDataPredicate(DummyData.SENSOR_NAME + "0", calendar.getTime(), null))
				  					  .collect(Collectors.toList());
		assertTrue(result.size() > 0);
	}

	@Test
	public void testSensorDataPredicateWithNameAndFrom2() {
		Calendar calendar = new GregorianCalendar(2999, 01, 01);
		List<SensorData> result = list.stream()
				                      .filter(new SensorDataPredicate(DummyData.SENSOR_NAME + "0", calendar.getTime(), null))
				                      .collect(Collectors.toList());
		assertEquals(0, result.size());
	}
}
