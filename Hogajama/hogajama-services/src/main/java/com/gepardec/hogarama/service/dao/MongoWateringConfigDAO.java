package com.gepardec.hogarama.service.dao;

import java.util.List;

import javax.inject.Inject;

import org.mongodb.morphia.Datastore;

import com.gepardec.hogarama.domain.watering.WateringConfigDAO;
import com.gepardec.hogarama.domain.watering.WateringConfigData;
import com.gepardec.hogarama.service.MongoDbProducer;

public class MongoWateringConfigDAO implements WateringConfigDAO{
	
	@Inject
	public Datastore db;

	public MongoWateringConfigDAO() {
	}
	
	@Override
	public void save(WateringConfigData wconf) {
		db.save(wconf);
	}

	@Override
	public WateringConfigData getBySensorName(String sensorName) {
		List<WateringConfigData> configs = db.createQuery(WateringConfigData.class).field("sensorName").equal(sensorName).asList();
		if (configs.isEmpty()){
			return null;
		}
		return configs.get(0);
	}

	public void setUpForTest() {
		MongoDbProducer producer = new MongoDbProducer();
		db = producer.getDatastore();
	}

}