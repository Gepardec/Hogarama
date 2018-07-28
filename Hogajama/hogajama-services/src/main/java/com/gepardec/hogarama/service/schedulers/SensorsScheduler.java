package com.gepardec.hogarama.service.schedulers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.gepardec.hogarama.domain.sensor.SensorDAO;

@Startup
@Singleton
public class SensorsScheduler {

	@Inject
	private Logger log;
	
	@Inject
	@Named("habaramaDao")
	private SensorDAO habaramaDao;
	
	private List<String> sensorNames = new ArrayList<>();
	
	@Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 second", persistent = false)
	public void getSensors() {
		log.info("Load the sensorNames from the database");
		loadSensorsFromDB();
	}

	public synchronized void loadSensorsFromDB() {
		sensorNames.clear();
		sensorNames = habaramaDao.getAllSensors();
	}
	
	public List<String> getSensorNames() {
		if(sensorNames.isEmpty()) {
			loadSensorsFromDB();
		}
		return sensorNames;
	}

}
