package com.gepardec.hogarama.domain;

public class Habarama {

	private long id;
	private SensorData sensorData;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public SensorData getSensorData() {
		return sensorData;
	}
	public void setSensorData(SensorData sensorData) {
		this.sensorData = sensorData;
	}
	
	public String toString() {
		return "{\"id\":" + id + ", \"temperature\":" + sensorData.getTemp() + ", \"kommentar\":\"" + sensorData.getComment() + "\"}" ;
	}
	
}