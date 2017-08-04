package com.gepardec.hogarama.service;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;


@ApplicationScoped
public class MongoDbProducer {

	private static final String HOGAJAMA_DB = "hogajamadb";
	private static final String USER = "hogajama";
	private static final char[] PASSWORD = System.getProperty("MONGODB_PW").toCharArray();
	private static final int PORT = 27017;
	private static final String COLLECTION = "habarama";
	private static final String HOST = System.getProperty("mongo.host", "mongodb");

	
	@Produces 
	public Datastore datastore() {
		MongoClient mongoClient = getClient();
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.gepardec.hogarama.domain");
		Datastore datastore = morphia.createDatastore(mongoClient, MongoDbProducer.HOGAJAMA_DB);
		datastore.ensureIndexes();
		return datastore;
	}
	
	@Produces 
	public DBCollection collection() {
		MongoClient mongoClient = getClient();
		DB db = mongoClient.getDB(HOGAJAMA_DB);
		return db.getCollection(COLLECTION);
	}
	
	
	private MongoClient getClient() {
		MongoCredential credential = MongoCredential.createCredential(USER, HOGAJAMA_DB, PASSWORD);
		return new MongoClient(new ServerAddress(HOST, PORT), Arrays.asList(credential));
	}
}