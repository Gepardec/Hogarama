package com.gepardec.hogarama.domain;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
//import java.util.Date;
import java.util.Date;

import javax.persistence.Entity;

import org.codehaus.jackson.map.ObjectMapper;

@Entity
public class SensorData {
	
	private long id;
	private LocalDate timestamp;
	private String sensorName;
	private String type;
	private double value;
	private String location;
	private String version;

	//TODO: format timestamp
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";

		try {
			json = mapper.writeValueAsString(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
