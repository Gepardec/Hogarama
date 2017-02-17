package com.gepardec.hogarama.service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.MongoClient;

@ApplicationScoped
public class MongoDbClientProducer {

	@Produces
	public MongoClient mongoClient() {
		return new MongoClient("localhost", 27017);
	}
}