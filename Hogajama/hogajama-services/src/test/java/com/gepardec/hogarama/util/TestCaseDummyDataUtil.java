package com.gepardec.hogarama.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gepardec.hogarama.domain.SensorData;

public class TestCaseDummyDataUtil {

	private Calendar calendar;
	private List<SensorData> sensors;

	@Before
	public void setUp() {
		calendar = new GregorianCalendar();
		sensors = DummyDataUtil.getDummySensorData(2, 2, true);
	}

	@Test
	public void testGenerateData() {
		assertEquals(4, sensors.size());

		SensorData sensorData1 = sensors.get(0);
		SensorData sensorData2 = sensors.get(1);

		assertTrue(sensorData1.getSensorName().equals(sensorData2.getSensorName()));
		assertTrue(sensorData1.getLocation().equals(sensorData2.getLocation()));
		assertTrue(sensorData1.getVersion().equals(sensorData2.getVersion()));
		assertTrue(sensorData1.getValue() > DummyDataUtil.MIN_VAL);
		assertTrue(sensorData1.getValue() < DummyDataUtil.MAX_VAL);
	}
	
	@Test
	public void testGenerateSameData() {
		List<SensorData> sensor2 = DummyDataUtil.getDummySensorData(99, 99, false);
		
		assertTrue(sensors.size() == 4);
		assertTrue(sensors.size() == sensor2.size());
		assertTrue(sensors.get(0).equals(sensor2.get(0)));
	}

	@Test
	public void testSetYear() {
		DummyDataUtil.setYear(calendar);
		assertEquals(2018, calendar.get(Calendar.YEAR));
	}

	@Test
	public void testSetMonth() {
		DummyDataUtil.setMonth(calendar);
		int month = calendar.get(Calendar.MONTH);
		assertTrue(month >= 0);
		assertTrue(month <= 11);
	}
	
	@Test
	public void testSetDay() {
		DummyDataUtil.setDay(calendar);
		int month = calendar.get(Calendar.DAY_OF_MONTH);
		assertTrue(month >= 1);
		assertTrue(month <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	@Test
	public void testSetHour() {
		DummyDataUtil.setHour(calendar);
		int month = calendar.get(Calendar.HOUR);
		assertTrue(month >= 0);
		assertTrue(month <= 23);
	}
	
	@Test
	public void testSetMinute() {
		DummyDataUtil.setMinute(calendar);
		int month = calendar.get(Calendar.MINUTE);
		assertTrue(month >= 0);
		assertTrue(month <= 59);
	}
}
