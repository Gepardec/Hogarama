package com.gepardec.hogarama.service.dao;

import java.util.Date;
import java.util.List;

import com.gepardec.hogarama.domain.SensorData;

public interface HabaramaDAO {

	public List<String> getAllSensors();
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to);
	public String getLocationBySensorName(String sensorName);
}
