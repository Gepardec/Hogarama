package com.gepardec.hogarama.service;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;


@ApplicationScoped
public class MongoDbDatastoreProducer {

	private static final String HOGAJAMA_DB = "hogajamadb";
	
	@Produces 
	public Datastore datastore() {
		String user = "hogajama";
		String database = HOGAJAMA_DB;
		char[] password = "hogajama@mongodb".toCharArray();

		MongoCredential credential = MongoCredential.createCredential(user, database, password);
		MongoClient mongoClient = new MongoClient(new ServerAddress("mongodb", 27017), Arrays.asList(credential));
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.gepardec.hogarama.domain");
		Datastore datastore = morphia.createDatastore(mongoClient, MongoDbDatastoreProducer.HOGAJAMA_DB);
		datastore.ensureIndexes();
		return datastore;
	}
}