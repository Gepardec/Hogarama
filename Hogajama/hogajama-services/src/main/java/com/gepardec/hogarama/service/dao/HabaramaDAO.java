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
	
	public String queryDataFromMongoDb() {
		return getAllEntries(-1);
	}
	
	public String queryDataFromMongoDb(int maxNumber) {
		return getAllEntries(maxNumber);
	}
	
	public String getAllEntries(int maxNumber) {
		
		list = new ArrayList<>();
		String result = new String();
		MongoDatabase database = mongoClient.getDatabase(MongoDbClientProducer.HOGAJAMA_DB);
		MongoCollection<Document> collection = database.getCollection(HABARAMA_JSON);
		
		collection.find().forEach(printBlock);
		int lowerBound = list.size() - maxNumber;
		if ( maxNumber == -1 || maxNumber > list.size()) {
			lowerBound = 0;
		}
		
		for (int x = list.size()-1; x >= lowerBound; x--) {
			result = result + '\n' + list.get(x);
		}
		
		return result;
		
	}

}