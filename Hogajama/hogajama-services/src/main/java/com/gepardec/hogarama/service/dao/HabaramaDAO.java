package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.gepardec.hogarama.domain.Habarama;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Model
public class HabaramaDAO {

	private static final String HABARAMA_JSON="habarama";
	private static final String HOGAJAMA_DB="hogajamadb";
	
	@Inject
	private MongoClient mongoClient;

	List<Habarama> listBooks;

	public List<Habarama> query() {
		
		DB db = mongoClient.getDB(HOGAJAMA_DB);
				
		DBCollection coll = db.getCollection(HABARAMA_JSON);
		DBCursor cursor = coll.find();

		List<Habarama> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				//TODO: Habrama Liste befüllen.
			}
		} finally {
			cursor.close();
		}
		return list;
	}
}