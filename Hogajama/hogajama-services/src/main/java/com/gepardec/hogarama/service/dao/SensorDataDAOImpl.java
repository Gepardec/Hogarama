package com.gepardec.hogarama.service.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.watering.WateringData;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@MongoDAO
@ApplicationScoped
public class SensorDataDAOImpl implements SensorDataDAO {

    @Inject
    private Datastore datastore;

    @Override
    public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {

        Metrics.requestsTotal.labels("hogajama_services", "getAllData").inc();
        Query<SensorData> query = datastore.createQuery(SensorData.class).order("-_id");
        limitQueryBySensor(sensorName, query);
        limitQueryByDate(from, to, query);

        FindOptions numberLimitOption = getFindOptionsWithMaxNumber(maxNumber);
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

    private FindOptions getFindOptionsWithMaxNumber(Integer maxNumber) {

        FindOptions findOptions = new FindOptions();
        if (maxNumber != null && maxNumber >= 0) {
            findOptions.limit(maxNumber);
        }
        return findOptions;
    }

    public void save(SensorData data){
        datastore.save(data);
    }

    @Override
    public void saveActorEvent(WateringData data) {
        datastore.save(data);
        
    }

}
