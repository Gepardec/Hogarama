package com.gepardec.hogarama.service.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;

@RequestScoped
public class SensorDAOImpl implements SensorDAO {

	@Inject
	private Datastore datastore;
	@Inject
	private MongoCollection<Document> collection;

	@Inject
	SensorNormalizer sensorNormalizer;
	
	@Override
	public List<String> getAllSensors() {
		DistinctIterable<String> sensors = collection.distinct("sensorName", String.class);
		return createResultList(sensors);
	}

	private <T> List<T> createResultList(MongoIterable<T> sourceIterable) {
		List<T> result = new ArrayList<>();
		sourceIterable.into(result);
		return result;
	}

	@Override
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
		limitQueryBySensor(sensorName, query);
		limitQueryByDate(from, to, query);

		FindOptions numberLimitOption = getFindOptionsWithMaxNumber(maxNumber);
		return sensorNormalizer.normalize(query.asList(numberLimitOption));
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

	private FindOptions getFindOptionsWithMaxNumber(Integer maxNumber) {
		FindOptions findOptions = new FindOptions();
		if (maxNumber != null && maxNumber >= 0) {
			findOptions.limit(maxNumber);
		}
		return findOptions;
	}

	@Override
	/**
	 * TODO: rewrite query and logic with single result
	 */
	public String getLocationBySensorName(String sensorName) {
		Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
		limitQueryBySensor(sensorName, query);

		FindOptions numberLimitOption = getFindOptionsWithMaxNumber(1);
		List<SensorData> sensors = query.asList(numberLimitOption);
		if(!sensors.isEmpty()) {
			return sensors.get(0).getLocation();
		} else {
			throw new NoResultException("Could not find location by sensorName");
		}
	}

}