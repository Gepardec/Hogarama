package com.gepardec.hogarama.service.predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.gepardec.hogarama.domain.SensorData;
import com.gepardec.hogarama.util.DummyDataUtil;

public class TestCaseSensorDataPredicate {

	private List<SensorData> list = DummyDataUtil.getDummySensorData(2, 100, true);

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
									  .filter(new SensorDataPredicate(DummyDataUtil.SENSOR_NAME + "0", null, null))
									  .collect(Collectors.toList());
		assertTrue(result.size() > 0);
	}

	@Test
	public void testSensorDataPredicateWithNameAndFrom1() {
		Calendar calendar = new GregorianCalendar(1990, 01, 01);
		List<SensorData> result = list.stream()
				  					  .filter(new SensorDataPredicate(DummyDataUtil.SENSOR_NAME + "0", calendar.getTime(), null))
				  					  .collect(Collectors.toList());
		assertTrue(result.size() > 0);
	}

	@Test
	public void testSensorDataPredicateWithNameAndFrom2() {
		Calendar calendar = new GregorianCalendar(2999, 01, 01);
		List<SensorData> result = list.stream()
				                      .filter(new SensorDataPredicate(DummyDataUtil.SENSOR_NAME + "0", calendar.getTime(), null))
				                      .collect(Collectors.toList());
		assertEquals(0, result.size());
	}
}
