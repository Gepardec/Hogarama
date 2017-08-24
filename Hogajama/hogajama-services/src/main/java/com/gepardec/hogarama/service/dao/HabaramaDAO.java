package com.gepardec.hogarama.service.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.gepardec.hogarama.domain.SensorData;
import com.mongodb.DBCollection;

@Model
public class HabaramaDAO {
	
	@Inject
	private Datastore datastore;
	
	@Inject
	private DBCollection collection;

	public List<String> getAllSensors() {
		List<String> sensorNames = collection.distinct("sensorName");		
		return sensorNames;
	}
	
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
		query = limitQueryBySensor(sensorName, query);
		query = query.order("-_id");
		query = limitQueryByDate(from, to, query);
		query = limitQueryByMaxNumber(maxNumber, query);

		return query.asList();
	}
	
	private Query<SensorData> limitQueryByMaxNumber(Integer maxNumber, Query<SensorData> query) {
		if(maxNumber != null) {
			query = query.limit(maxNumber);
		}
		return query;
	}

	private Query<SensorData> limitQueryBySensor(String sensorName, Query<SensorData> query) {
		if(sensorName != null && !sensorName.isEmpty()) {
			query = query.field("sensorName").equal(sensorName);
		}
		return query;
	}

	private Query<SensorData> limitQueryByDate(Date from, Date to, Query<SensorData> query) {
		if(from != null) {
			if(to == null) {
				to = new Date();
			}
			query = query.field("time").greaterThanOrEq(from);
			query = query.field("time").lessThanOrEq(to);
			
		}
		return query;
	}
	
}