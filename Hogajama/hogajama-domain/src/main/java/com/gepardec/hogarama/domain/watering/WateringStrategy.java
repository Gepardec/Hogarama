package com.gepardec.hogarama.domain.watering;

import static com.gepardec.hogarama.domain.DateUtils.toDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;

public class WateringStrategy {
 
	public enum Config{

		DEFAULT(60, 0.2, 5);
		
		protected int measureInterval;
		protected double lowWater;
		protected int waterDuration;
		
		Config( int measureInterval, double lowWater, int waterDuration) {
			this.waterDuration = waterDuration;
			this.measureInterval = measureInterval;
			this.lowWater = lowWater;
		}
	}
	
	@Inject
	private SensorDAO database;
	
	@Inject
	private ActorService actor;
	
	private LocalDateTime now;

	private Config config;

	
	public WateringStrategy() {
		this.config = Config.DEFAULT;
	}

	public void waterAll() {
		for (String sensorName : database.getAllSensors()) {
			int dur = water(sensorName, getDate());
			if ( dur > 0 ) {
				actor.sendActorMessage(database.getLocationBySensorName(sensorName), sensorName, dur);
			}
		}
	}
	
	public int water(String sensorName, LocalDateTime now) {
		
		List<SensorData> data = database.getAllData(200, sensorName, toDate(now.minus(Duration.ofMinutes(config.measureInterval))), toDate(now.plus(Duration.ofSeconds(1))));
		double sum = 0;
		for (SensorData sensorData : data) {
			sum += sensorData.getValue();
		}
		double avg = sum / data.size();
		if ( avg < config.lowWater ) {
			return config.waterDuration;
		}
		else return 0;
	}
	
	protected void setSensorDAO(SensorDAO database) {
		this.database = database;
	}

	protected void setActor(ActorService actor) {
		this.actor = actor;
	}

	protected void setDate(LocalDateTime now) {
		this.now = now;
	}

	private LocalDateTime getDate() {
		if ( null != now) {
			return now;
		}
		return LocalDateTime.now();
	}

}
