package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.bson.Document;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.gepardec.hogarama.service.CassandraClient;
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
	
	@Inject
	private CassandraClient cassandraClient;

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
		int lowerBound = list.size() - maxNumber;
		if ( maxNumber == -1 || maxNumber > list.size()) {
			lowerBound = 0;
		}
		
		for (int x = list.size()-1; x >= lowerBound; x--) {
			result = result + '\n' + list.get(x);
		}
		
		return result;
		
	}
	
	public String retrieveDataFromCassandra() {
		try {
			ResultSet results = cassandraClient.getSession().execute("select * from Hogarama.sensors;");
			StringBuilder stringBuilder = new StringBuilder();
			for (Row row : results) {
				System.out.println(row.toString());
				stringBuilder.append(row.toString()).append(System.getProperty("line.separator"));
			}
			return stringBuilder.toString();
		} finally {
			cassandraClient.closeSessionAndCluster();
		}
	}

}