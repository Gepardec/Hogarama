package com.gepardec.hogarama.domain.watering;

import static com.gepardec.hogarama.domain.DateUtils.toDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;

public class WateringStrategy {

	@Inject
	@Named("habaramaDao")
	SensorDAO database;
	
	public boolean water(String sensorName, LocalDateTime now) {
		
		List<SensorData> data = database.getAllData(200, sensorName, toDate(now.minus(Duration.ofMinutes(60))), toDate(now.plus(Duration.ofSeconds(1))));
		double sum = 0;
		for (SensorData sensorData : data) {
			sum += sensorData.getValue();
		}
		double avg = sum / data.size();
		return avg < 0.2;
	}
	
	public void setHabaramaDAO(SensorDAO database) {
		this.database = database;
	}

}
