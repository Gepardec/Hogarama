package com.gepardec.hogarama.domain.watering;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("watering")
public class WateringConfigData {
	
	@Id
	private String sensorName;
	private String actorName;
	private int measureInterval;
	private double lowWater;
	private int waterDuration;

	public WateringConfigData() {
		
	}
	
	public WateringConfigData(String id, String actorName, int measureInterval, double lowWater, int waterDuration) {
		super();
		this.sensorName = id;
		this.actorName = actorName;
		this.measureInterval = measureInterval;
		this.lowWater = lowWater;
		this.waterDuration = waterDuration;
	}

	public String getSensorName() {
		return sensorName;
	}

	public String getActorName() {
		return actorName;
	}

	public long getMeasureInterval() {
		return measureInterval;
	}

	public double getLowWater() {
		return lowWater;
	}	
	
	public int getWaterDuration() {
		return waterDuration;
	}	
}
