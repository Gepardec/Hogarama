package com.gepardec.hogarama.domain.sensor;

import java.util.Date;
import java.util.List;

public interface SensorDAO {

	public List<String> getAllSensors();
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to);
	public String getLocationBySensorName(String sensorName);
}
