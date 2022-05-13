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
import com.gepardec.hogarama.domain.watering.WateringDataDAO;
import com.gepardec.hogarama.domain.watering.WateringData;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;
import static com.gepardec.hogarama.service.CouchbaseProducer.BUCKET_NAME;
import static com.gepardec.hogarama.service.CouchbaseProducer.SCOPE_NAME;
import static com.gepardec.hogarama.util.couchbase.CouchbaseUtil.getKey;

@CouchbaseDAO
public class CouchbaseWateringDataDAO implements WateringDataDAO, Serializable {

  @Inject
  private Scope scope;

  private Collection collection;

  private static final String COLLECTION_NAME = "wateringData";

  @PostConstruct
  private void setup() {
    collection = scope.collection(COLLECTION_NAME);
  }

  @Override
  public List<WateringData> getWateringData(Integer maxNumber, String actorName, Date from, Date to) {
    return getWateringData(maxNumber, actorName, from, to, false);
  }

  List<WateringData> getWateringData(Integer maxNumber, String actorName, Date from, Date to, boolean enforceConsistency) {
    QueryScanConsistency consistency;

    if (enforceConsistency) {
      consistency = QueryScanConsistency.REQUEST_PLUS;
    } else {
      consistency = QueryScanConsistency.NOT_BOUNDED;
    }

    Metrics.requestsTotal.labels("hogajama_services", "getWateringData");

    // @formatter:off
    String statement = String.format(
        "SELECT %s.* "
            + "from %s.%s.%s "
            + "where name = $actorName "
            + "and time >= $from "
            + "and time <= $to "
            + "limit $maxNumber",
        COLLECTION_NAME,
        BUCKET_NAME,
        SCOPE_NAME,
        COLLECTION_NAME
    );

    QueryOptions options = queryOptions().parameters(JsonObject.create()
        .put("actorName", actorName)
        .put("from", from.getTime())
        .put("to", to.getTime())
        .put("maxNumber", maxNumber)
    ).scanConsistency(consistency);
    // @formatter:on

    try {
      return scope.query(statement, options).rowsAs(WateringData.class);
    } catch (QueryException | InternalServerFailureException e) {
      throw new TechnicalException("Query Failed: " + statement, e);
    }
  }

  @Override
  public void save(WateringData wateringData) {
    try {
      collection.insert(getKey(COLLECTION_NAME, wateringData.getId()), wateringData);
    } catch (DocumentExistsException | TimeoutException ex) {
      throw new TechnicalException("Failure while trying to create sensorData: " + wateringData, ex);
    }
  }
}
