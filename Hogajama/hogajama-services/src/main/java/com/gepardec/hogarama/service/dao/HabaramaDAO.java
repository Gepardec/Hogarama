package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.bson.Document;

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

	List<String> list;

	Block<Document> printBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			list.add(document.toJson());
		}
	};

	public String query() {
		return getAllEntries(-1);

	}
	
	public String query(int maxNumber) {
		return getAllEntries(maxNumber);
	}
	
	public String getAllEntries(int maxNumber) {
		list = new ArrayList<>();
		String result = new String();
		MongoDatabase database = mongoClient.getDatabase(MongoDbClientProducer.HOGAJAMA_DB);
		MongoCollection<Document> collection = database.getCollection(HABARAMA_JSON);
		collection.find().forEach(printBlock);
		if(maxNumber == -1) {
			maxNumber = list.size();
		}
		if(maxNumber > list.size()) {
			maxNumber = list.size();
		}
		
		for ( int x = 0; x < maxNumber; x++ ) {
			result = result + '\n' + list.get(x);
		}
		
		return result;
		
	}

}