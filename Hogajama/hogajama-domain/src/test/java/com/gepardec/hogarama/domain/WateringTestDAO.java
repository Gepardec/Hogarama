package com.gepardec.hogarama.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.gepardec.hogarama.service.dao.HabaramaDAO;

public class WateringTestDAO implements HabaramaDAO {

	private List<SensorData> data;

	public WateringTestDAO(List<SensorData> list) {
		this.data = list;
	}

	@Override
	public List<String> getAllSensors() {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
		
		return data.stream().filter(sd -> from.before(sd.getTime()) && to.after(sd.getTime())).collect(Collectors.toList());
	}

	@Override
	public String getLocationBySensorName(String sensorName) {
		throw new RuntimeException("Not Implemented");
	}

}
