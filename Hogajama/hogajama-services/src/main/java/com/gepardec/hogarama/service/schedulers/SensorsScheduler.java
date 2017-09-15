package com.gepardec.hogarama.service.schedulers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.mongodb.DBCollection;

@Startup
@Singleton
public class SensorsScheduler {

	
	@Inject
	private Logger log;
	
	@Inject
	private DBCollection collection;
	
	private List<String> sensorNames;
	
	@PostConstruct
	public void init() {
		sensorNames = new ArrayList<>();
	}

	@Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 second")
	public void getSensors() {
		log.info("Load the sensorNames");
		getAllSensors();
	}

	public void getAllSensors() {
		sensorNames = collection.distinct("sensorName");		
	}
	
	public List<String> getSensorNames() {
		if(sensorNames.isEmpty()) {
			getAllSensors();
		}
		return sensorNames;
	}

	public void setSensorNames(List<String> sensorNames) {
		this.sensorNames = sensorNames;
	}
	
}
