package com.gepardec.hogarama.service.dao;

import java.io.IOException;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.gepardec.hogarama.domain.SensorData;

@Model
public class HabaramaDAO {
	
	@Inject
	private Datastore datastore;
	
	List<String> list;
	
	public String getAllSensorData() {
		final Query<SensorData> query = datastore.createQuery(SensorData.class).order("_id");
		final List<SensorData> sensorData = query.asList();
		return generateJson(sensorData);
	}
	
	public String getAllSensorData(int maxNumber) {
		final Query<SensorData> query = datastore.createQuery(SensorData.class).order("_id").limit(maxNumber);
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

}