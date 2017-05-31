package com.gepardec.hogarama.service.dao;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.gepardec.hogarama.domain.SensorData;
import com.gepardec.hogarama.service.JsonFormatter;
import com.mongodb.DBCollection;

@Model
public class HabaramaDAO {
	
	@Inject
	private Datastore datastore;
	
	@Inject
	private DBCollection collection;
	
	public Object getAllData(Integer maxNumber, String sensor) {
		if( sensor != null ) {
			return getDataBySensorName(sensor, maxNumber);
		} else {
			return getAllSensorData(maxNumber);
		}
	}
	
	public List<SensorData> getAllSensorData(Integer maxNumber) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
		if(maxNumber != null) {
			query = query.limit(maxNumber);
		}
		final List<SensorData> sensorData = query.asList();
//		return JsonFormatter.generateJson(sensorData);
		return sensorData;
	}
	
	public List<String> getAllSensors() {
		List<String> sensorNames = collection.distinct("sensorName");		
//		return JsonFormatter.generateJson(sensorNames);
		return sensorNames;
	}
	
	public List<SensorData> getDataBySensorName(String sensorName, Integer maxNumber) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).field("sensorName").equal(sensorName).order("-_id");
		if(maxNumber != null) {
			query = query.limit(maxNumber);
		}
		final List<SensorData> sensorData = query.asList();
		return sensorData;
//		return JsonFormatter.generateJson(sensorData);
	}
	
}