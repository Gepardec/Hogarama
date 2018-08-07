package com.gepardec.hogarama.domain.watering;

import static com.gepardec.hogarama.domain.DateUtils.toDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;

public enum WateringStrategy {
 
	DEFAULT(60, 0.2, 5);
	
	@Inject
	SensorDAO database;
	
	private int measureInterval;
	private double lowWater;
	private int waterDuration;
	

	WateringStrategy( int measureInterval, double lowWater, int waterDuration) {
		this.waterDuration = waterDuration;
		this.measureInterval = measureInterval;
		this.lowWater = lowWater;
	}

	
	public int water(String sensorName, LocalDateTime now) {
		
		List<SensorData> data = database.getAllData(200, sensorName, toDate(now.minus(Duration.ofMinutes(measureInterval))), toDate(now.plus(Duration.ofSeconds(1))));
		double sum = 0;
		for (SensorData sensorData : data) {
			sum += sensorData.getValue();
		}
		double avg = sum / data.size();
		if ( avg < lowWater ) {
			return waterDuration;
		}
		else return 0;
	}
	
	public void setSensorDAO(SensorDAO database) {
		this.database = database;
	}

}
