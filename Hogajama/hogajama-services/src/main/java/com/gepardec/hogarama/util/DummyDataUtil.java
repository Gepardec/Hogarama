package com.gepardec.hogarama.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.gepardec.hogarama.domain.SensorData;

public class DummyDataUtil {

	public static final String UNKNOW_LOCATION = "unknown location";
	public static final int MIN_VAL = 0;
	public static final int MAX_VAL = 100;
	public static final String SENSOR_TYPE = "wasser";
	public static final String SENSOR_NAME = "dummySensor";
	public static final String SENSOR_VERSION = "1";
	public static final String SENSOR_LOCATION = "dummyLocation";
	private static final List<SensorData> sensorDatas = new ArrayList<>();
	private static final Random RANDOM = new Random();

	private DummyDataUtil() {
		//static
	}

	public static List<SensorData> getDummySensorData(int numberOfSensor, int numberOfData, boolean newData) {
		if (sensorDatas.isEmpty() || newData) {
			sensorDatas.clear();
			generateDummySensorData(numberOfSensor, numberOfData);
		}
		return new ArrayList<>(sensorDatas);
	}

	private static void generateDummySensorData(int numberOfSensor, int numberOfData) {
		for (int i = 0; i < numberOfSensor; i++) {
			String sensorName = SENSOR_NAME + i;
			String type = SENSOR_TYPE;
			String version = SENSOR_VERSION;
			String location = SENSOR_LOCATION + i;

			for (int j = 0; j < numberOfData; j++) {
				String id = UUID.randomUUID().toString();
				Date time = getRandomTime();
				double value = getRandomIntBetween(MIN_VAL, MAX_VAL);
				sensorDatas.add(new SensorData(id, time, sensorName, type, value, location, version));
			}
		}
	}

	public static Date getRandomTime() {
		Calendar gc = new GregorianCalendar();
		setRandomDate(gc);
		return gc.getTime();
	}

	private static void setRandomDate(Calendar gc) {
		setYear(gc);
		setMonth(gc);
		setDay(gc);
		setHour(gc);
		setMinute(gc);
	}

	public static void setYear(Calendar gc) {
		int year = 2018;
		gc.set(GregorianCalendar.YEAR, year);
	}

	public static void setMonth(Calendar gc) {
		int monthOfYear = getRandomIntBetween(0, 11);
		gc.set(GregorianCalendar.MONTH, monthOfYear);
	}

	public static void setDay(Calendar gc) {
		int dayOfMonth = getRandomIntBetween(1, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
		gc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	}

	public static void setHour(Calendar gc) {
		int hourOfDay = getRandomIntBetween(0, 23);
		gc.set(Calendar.HOUR_OF_DAY, hourOfDay);
	}

	public static void setMinute(Calendar gc) {
		int minuteOfHour = getRandomIntBetween(0, 59);
		gc.set(Calendar.MINUTE, minuteOfHour);
	}

	public static int getRandomIntBetween(int start, int end) {
		return start + RANDOM.nextInt(end-start);
	}
}
