package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import com.gepardec.hogarama.domain.SensorData;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;

@Model
public class HabaramaDAO {

	@Inject
	private Datastore datastore;
	@Inject
	private MongoCollection<Document> collection;

	public List<String> getAllSensors() {
		DistinctIterable<String> sensors = collection.distinct("sensorName", String.class);
		return createResultList(sensors);
	}

	private <T> List<T> createResultList(MongoIterable<T> sourceIterable) {
		List<T> result = new ArrayList<>();
		sourceIterable.into(result);
		return result;
	}

	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
		limitQueryBySensor(sensorName, query);
		limitQueryByDate(from, to, query);

		FindOptions numberLimitOption = getFindOptionsWithMaxNumber(maxNumber, query);
		return query.asList(numberLimitOption);
	}

	private void limitQueryBySensor(String sensorName, Query<SensorData> query) {
		if (StringUtils.isNotEmpty(sensorName)) {
			query.field("sensorName").equal(sensorName);
		}
	}

	private void limitQueryByDate(Date from, Date to, Query<SensorData> query) {
		if (from != null) {
			if (to == null) {
				to = new Date();
			}
			query.field("time").greaterThanOrEq(from);
			query.field("time").lessThanOrEq(to);
		}
	}

	private FindOptions getFindOptionsWithMaxNumber(Integer maxNumber, Query<SensorData> query) {
		FindOptions findOptions = new FindOptions();
		if (maxNumber != null) {
			findOptions.limit(maxNumber);
		}
		return findOptions;
	}

}