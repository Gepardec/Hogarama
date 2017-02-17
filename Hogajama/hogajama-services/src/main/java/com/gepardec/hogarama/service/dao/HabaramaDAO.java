package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.bson.Document;

import com.gepardec.hogarama.domain.Habarama;
import com.gepardec.hogarama.domain.SensorData;
import com.gepardec.hogarama.service.MongoDbClientProducer;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Model
public class HabaramaDAO {

	private static final String HABARAMA_JSON = "habarama";

	@Inject
	private MongoClient mongoClient;

	List<Habarama> list;
	
	Habarama habarama;

	Block<Document> printBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			habarama = new Habarama();
			habarama.setId((Integer) document.get("habarama_id"));
			SensorData data = new SensorData();
			Document sensorData = (Document) document.get("sensor_data");
			data.setComment((String) sensorData.get("comment"));
			data.setTemp((Double) sensorData.get("temp"));
			habarama.setSensorData(data);
			list.add(habarama);
			System.out.println(document.toJson());
		}
	};

	public List<Habarama> query() {
		list = new ArrayList<>();
		MongoDatabase database = mongoClient.getDatabase(MongoDbClientProducer.HOGAJAMA_DB);
		MongoCollection<Document> collection = database.getCollection(HABARAMA_JSON);
		collection.find().forEach(printBlock);
		return list;

	}

}