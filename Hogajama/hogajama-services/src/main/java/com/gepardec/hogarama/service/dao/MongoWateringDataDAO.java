package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.watering.WateringDataDAO;
import com.gepardec.hogarama.domain.watering.WateringData;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@MongoDAO
public class MongoWateringDataDAO implements WateringDataDAO {

  @Inject
  private Datastore dataStore;

  @Override
  public List<WateringData> getWateringData(Integer maxNumber, String actorName, Date from, Date to) {

    Metrics.requestsTotal.labels("hogajama_services", "getWateringData");
    Query<WateringData> query = dataStore.createQuery(WateringData.class).order("-_id");
    limitQueryByActor(actorName, query);
    limitQueryByDate(from, to, query);

    FindOptions numberLimitOption = getFindOptionsWithMaxNumber(maxNumber);
    return query.asList(numberLimitOption);
  }

  @Override
  public void save(WateringData wateringData) {
    dataStore.save(wateringData);
  }

  private void limitQueryByActor(String sensorName, Query<WateringData> query) {

    if (StringUtils.isNotEmpty(sensorName)) {
      query.field("name").equal(sensorName);
    }
  }

  private void limitQueryByDate(Date from, Date to, Query<WateringData> query) {

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
}
