package com.gepardec.hogarama.service.dao;

import com.couchbase.client.core.error.QueryException;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.service.couchbase.CouchbaseProducer;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;

public class SensorDataCouchbaseDAOImpl implements SensorDataDAO {

  @Inject
  CouchbaseProducer producer;

  @Override
  public List<String> getAllSensors() {
    return null;
  }

  @Override
  public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
    String query="SELECT sensorData.* from `sensorData` where sensorName = $sensorName and time >= $from and time <= $to";

    List<SensorData> sensorData;
    try {
      QueryResult queryResult = producer.getScope().query(query,
          queryOptions().parameters(JsonObject.create()
              .put("sensorName", sensorName)
              .put("from", from).put("to", to)
          ));
      sensorData =  queryResult.rowsAs(SensorData.class);
    } catch (QueryException e) {
      throw new TechnicalException("Query Failed: " + query, e);
    }
    return sensorData;
  }

  @Override
  public String getLocationBySensorName(String sensorName) {
    return null;
  }

  @Override
  public void save(SensorData sensorData) {

  }
}
