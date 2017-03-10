package com.gepardec.hogarama.service;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@ApplicationScoped
public class MongoDbClientProducer {

	public static final String HOGAJAMA_DB = "hogajamadb";

	@Produces
	public MongoClient mongoClient() {

		String user = "hogajama";
		String database = HOGAJAMA_DB;
		char[] password = "hogajama@mongodb".toCharArray();

		MongoCredential credential = MongoCredential.createCredential(user, database, password);
		MongoClient mongoClient = new MongoClient(new ServerAddress("mongodb", 27017), Arrays.asList(credential));
		
		return mongoClient;
	}
}