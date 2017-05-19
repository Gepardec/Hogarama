package com.gepardec.hogarama.service.dao;

import java.io.IOException;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
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
	
	List<String> list;
	
	public String getAllSensorData() {
		final Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
		final List<SensorData> sensorData = query.asList();
		return generateJson(sensorData);
	}
	
	public String getAllSensorData(int maxNumber) {
		final Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id").limit(maxNumber);
		final List<SensorData> sensorData = query.asList();
		return generateJson(sensorData);
	}
	
	public String getAllSensors() {
		List<String> list = collection.distinct("sensorName");		
		return generateJsonForSensorName(list);
	}
	
	public String getDataBySensorName(String sensorName, int maxNumber) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).field("sensorName").equal(sensorName).order("-_id");
		if(maxNumber > 0) {
			query = query.limit(maxNumber);
		}
		final List<SensorData> sensorData = query.asList();
		return generateJson(sensorData);
	}
	
	//TODO: format timestamp
	public String generateJson(List<SensorData> sensorData) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";

		try {
			json = mapper.writeValueAsString(sensorData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	//TODO: only one generateJson-Method
	public String generateJsonForSensorName(List<String> sensorNames) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";

		try {
			json = mapper.writeValueAsString(sensorNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}




}