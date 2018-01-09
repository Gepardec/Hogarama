package com.gepardec.hogarama.service.schedulers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.bson.Document;
import org.slf4j.Logger;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;

@Startup
@Singleton
public class SensorsScheduler {

	@Inject
	private Logger log;
	
	@Inject
	private MongoCollection<Document> collection;
	
	private List<String> sensorNames = new ArrayList<>();
	
	@Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 second")
	public void getSensors() {
		log.info("Load the sensorNames from the database");
		loadSensorsFromDB();
	}

	public void loadSensorsFromDB() {
		DistinctIterable<String> sensorNames = collection.distinct("sensorName", String.class);
		this.sensorNames.clear();
		sensorNames.into(this.sensorNames);
	}
	
	public List<String> getSensorNames() {
		if(sensorNames.isEmpty()) {
			loadSensorsFromDB();
		}
		return sensorNames;
	}

}
