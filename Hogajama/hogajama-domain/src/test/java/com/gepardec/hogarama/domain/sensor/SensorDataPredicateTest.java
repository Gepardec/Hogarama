package com.gepardec.hogarama.domain.sensor;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import com.gepardec.hogarama.dao.SensorDataPredicate;
import com.gepardec.hogarama.testdata.DummyData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SensorDataPredicateTest {

	private final List<SensorData> list = DummyData.getDummySensorData(2, 100, true);

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
		Calendar calendar = new GregorianCalendar(1990, Calendar.JANUARY, 1);
		List<SensorData> result = list.stream()
				  					  .filter(new SensorDataPredicate(DummyData.SENSOR_NAME + "0", calendar.getTime(), null))
				  					  .collect(Collectors.toList());
		assertTrue(result.size() > 0);
	}

	@Test
	public void testSensorDataPredicateWithNameAndFrom2() {
		Calendar calendar = new GregorianCalendar(2999, Calendar.JANUARY, 1);
		List<SensorData> result = list.stream()
				                      .filter(new SensorDataPredicate(DummyData.SENSOR_NAME + "0", calendar.getTime(), null))
				                      .collect(Collectors.toList());
		assertEquals(0, result.size());
	}
}
