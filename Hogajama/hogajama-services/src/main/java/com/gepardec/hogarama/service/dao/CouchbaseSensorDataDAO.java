package com.gepardec.hogarama.service.dao;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.InternalServerFailureException;
import com.couchbase.client.core.error.QueryException;
import com.couchbase.client.core.error.TimeoutException;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.gepardec.hogarama.annotations.CouchbaseDAO;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.util.couchbase.N1qlBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;
import static com.gepardec.hogarama.service.CouchbaseProducer.BUCKET_NAME;
import static com.gepardec.hogarama.service.CouchbaseProducer.SCOPE_NAME;
import static com.gepardec.hogarama.util.couchbase.CouchbaseUtil.getKey;

@CouchbaseDAO
public class CouchbaseSensorDataDAO implements SensorDataDAO, Serializable {

  @Inject
  private Scope scope;

  private Collection collection;

  private static final String COLLECTION_NAME = "sensorData";


  @PostConstruct
  private void setup() {
    collection = scope.collection(COLLECTION_NAME);
  }

  @Override
  public List<String> getAllSensors() {
    Metrics.requestsTotal.labels("hogajama_services", "getAllSensors").inc();

    String statement = new N1qlBuilder(BUCKET_NAME, SCOPE_NAME).select("sensorName").distinct().from(COLLECTION_NAME).build();

    try {
      // @formatter:off
      return scope
          .query(statement)
          .rowsAs(SensorData.class)
          .stream().map(SensorData::getSensorName)
          .collect(Collectors.toList());
      // @formatter:on
    } catch (QueryException e) {
      throw new TechnicalException("Query Failed: " + statement, e);
    }
  }

  @Override
  public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
    return getAllData(maxNumber, sensorName, from, to, false);
  }

  List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to, boolean enforceConsistency) {
    QueryScanConsistency consistency;

    if (enforceConsistency) {
      consistency = QueryScanConsistency.REQUEST_PLUS;
    } else {
      consistency = QueryScanConsistency.NOT_BOUNDED;
    }

    Metrics.requestsTotal.labels("hogajama_services", "getAllData").inc();

    //@formatter:off
    String statement = String.format(
        "SELECT %s.* "
            + "from %s.%s.%s "
            + "where sensorName = $sensorName "
            + "and time >= $from "
            + "and time <= $to ",
        COLLECTION_NAME,
        BUCKET_NAME,
        SCOPE_NAME,
        COLLECTION_NAME
    );

    if(maxNumber >= 0) {
      statement += "limit $maxNumber";
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    QueryOptions options = queryOptions().parameters(JsonObject.create()
            .put("sensorName", sensorName)
            .put("from", sdf.format(from))
            .put("to", sdf.format(to))
            .put("maxNumber", maxNumber)
    ).scanConsistency(consistency);
    //@formatter:on

    try {
      return scope.query(statement, options).rowsAs(SensorData.class);
    } catch (QueryException | InternalServerFailureException e) {
      throw new TechnicalException("Query Failed: " + statement, e);
    }
  }

  @Override
  public String getLocationBySensorName(String sensorName) {
    Metrics.requestsTotal.labels("hogajama_services", "getLocationBySensorName").inc();

    //@formatter:off
    String statement = String.format(
        "SELECT distinct location "
            + "from %s.%s.%s "
            + "where sensorName = $sensorName",
        BUCKET_NAME,
        SCOPE_NAME,
        COLLECTION_NAME
    );

    QueryOptions options = queryOptions().parameters(JsonObject.create()
            .put("sensorName", sensorName)
    );
    //@formatter:on

    List<SensorData> sensorData;
    try {
      sensorData = scope.query(statement, options).rowsAs(SensorData.class);
    } catch (QueryException e) {
      throw new TechnicalException("Query Failed: " + statement, e);
    }

    if (sensorData == null || sensorData.isEmpty()) {
      Metrics.exceptionsThrown.labels("hogarama_services", "NoResultException", "getLocationBySensorName").inc();
      throw new NoResultException("No location for sensorName: " + sensorName);
    } else if (sensorData.size() > 1) {
      Metrics.exceptionsThrown.labels("hogarama_services", "NonUniqueResultException", "getLocationBySensorName").inc();
      throw new NonUniqueResultException("Multiple locations for sensorName: " + sensorName);
    }
    return sensorData.get(0).getLocation();
  }

  @Override
  public void save(SensorData sensorData) {
    if (sensorData == null) {
      throw new TechnicalException("Failure while trying to create sensorData. SensorData must not be null.");
    }
    try {
      collection.insert(getKey(COLLECTION_NAME, sensorData.getId()), sensorData);
    } catch (DocumentExistsException | TimeoutException ex) {
      throw new TechnicalException("Failure while trying to create sensorData: " + sensorData, ex);
    }
  }

}
