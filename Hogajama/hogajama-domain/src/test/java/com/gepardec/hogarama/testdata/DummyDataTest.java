package com.gepardec.hogarama.testdata;

import com.gepardec.hogarama.domain.sensor.SensorData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DummyDataTest {

	private Calendar calendar;
	private List<SensorData> sensors;

	@BeforeEach
	public void setUp() {
		calendar = new GregorianCalendar();
		sensors = DummyData.getDummySensorData(2, 2, true);
	}

	@Test
	public void testGenerateData() {
		assertEquals(4, sensors.size());

		SensorData sensorData1 = sensors.get(0);
		SensorData sensorData2 = sensors.get(1);

		assertEquals(sensorData1.getSensorName(), sensorData2.getSensorName());
		assertEquals(sensorData1.getLocation(), sensorData2.getLocation());
		assertEquals(sensorData1.getVersion(), sensorData2.getVersion());
		assertTrue(sensorData1.getValue() > DummyData.MIN_VAL);
		assertTrue(sensorData1.getValue() < DummyData.MAX_VAL);
	}
	
	@Test
	public void testGenerateSameData() {
		List<SensorData> sensor2 = DummyData.getDummySensorData(99, 99, false);

		assertEquals(4, sensors.size());
		assertEquals(sensors.size(), sensor2.size());
		assertEquals(sensors.get(0), sensor2.get(0));
	}

	@Test
	public void testSetYear() {
		DummyData.setYear(calendar);
		assertEquals(2018, calendar.get(Calendar.YEAR));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void testSetMonth() {
		DummyData.setMonth(calendar);
		int month = calendar.get(Calendar.MONTH);
		assertTrue(month >= 0);
		assertTrue(month <= 11);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Test
	public void testSetDay() {
		DummyData.setDay(calendar);
		int month = calendar.get(Calendar.DAY_OF_MONTH);
		assertTrue(month >= 1);
		assertTrue(month <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	@SuppressWarnings("ConstantConditions")
	@Test
	public void testSetHour() {
		DummyData.setHour(calendar);
		int month = calendar.get(Calendar.HOUR);
		assertTrue(month >= 0);
		assertTrue(month <= 23);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Test
	public void testSetMinute() {
		DummyData.setMinute(calendar);
		int month = calendar.get(Calendar.MINUTE);
		assertTrue(month >= 0);
		assertTrue(month <= 59);
	}
}
