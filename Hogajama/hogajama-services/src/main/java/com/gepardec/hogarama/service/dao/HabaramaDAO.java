package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.bson.Document;

import com.gepardec.hogarama.domain.Habarama;
import com.gepardec.hogarama.service.MongoDbClientProducer;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Model
public class HabaramaDAO {

	private static final String HABARAMA_JSON = "habarama";

	@Inject
	private MongoClient mongoClient;

	List<Habarama> listBooks;

	Block<Document> printBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			System.out.println(document.toJson());
		}
	};

	public List<Habarama> query() {

		DB db = mongoClient.getDB(MongoDbClientProducer.HOGAJAMA_DB);

		MongoDatabase database = mongoClient.getDatabase(MongoDbClientProducer.HOGAJAMA_DB);
		MongoCollection<Document> collection = database.getCollection(HABARAMA_JSON);
		collection.find().forEach(printBlock);

		List<Habarama> list = new ArrayList<>();
		// TODO: Habrama Liste befüllen.
		return list;
	}

}