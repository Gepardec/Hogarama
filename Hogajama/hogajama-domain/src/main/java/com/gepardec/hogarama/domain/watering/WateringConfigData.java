package com.gepardec.hogarama.domain.watering;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("wateringConfig")
public class WateringConfigData implements WateringRule {
	
	@Id
	private String sensorName;
	private String actorName;
	private double lowWater;
	private int waterDuration;

	public WateringConfigData() {
		
	}
	
	public WateringConfigData(String id, String actorName, double lowWater, int waterDuration) {
		super();
		this.sensorName = id;
		this.actorName = actorName;
		this.lowWater = lowWater;
		this.waterDuration = waterDuration;
	}

	@Override
    public String getSensorName() {
		return sensorName;
	}

	@Override
    public String getActorName() {
		return actorName;
	}

	@Override
    public double getLowWater() {
		return lowWater;
	}	
	
	@Override
    public int getWaterDuration() {
		return waterDuration;
	}	
}
