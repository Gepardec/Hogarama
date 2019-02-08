package com.gepardec.hogarama.service;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@ApplicationScoped
public class MongoDbProducer {

	private static final String HOGAJAMA_DB = "hogajamadb";
	private static final String USER = "hogajama";
	// You need to define Environment Variable BEFORE Eclipse Start / Login Computer. 
	// You should define it in .bashrc: export MONGODB_PW=xxx
	private static final char[] PASSWORD = System.getenv("MONGODB_PW").toCharArray();
	private static final int PORT = 27017;
	private static final String COLLECTION = "habarama";
	private static final String HOST = System.getProperty("mongo.host", "localhost");
	private static MongoClient client;

	@Produces
	public Datastore getDatastore() {
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.gepardec.hogarama.domain");
		Datastore datastore = morphia.createDatastore(getClient(), MongoDbProducer.HOGAJAMA_DB);
		datastore.ensureIndexes();
		return datastore;
	}

	@Produces
	public MongoCollection<Document> getCollection() {
		MongoDatabase dataBase = getClient().getDatabase(HOGAJAMA_DB);
		return dataBase.getCollection(COLLECTION);
	}

	private static MongoClient getClient() {
		if (client == null) {
			MongoCredential credential = MongoCredential.createCredential(USER, HOGAJAMA_DB, PASSWORD);
			client = new MongoClient(new ServerAddress(HOST, PORT), Arrays.asList(credential));
		}
		return client;
	}
}