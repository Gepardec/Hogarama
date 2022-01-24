package com.gepardec.hogarama.service.dao;

import com.couchbase.client.core.error.QueryException;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.watering.WateringDAO;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.service.couchbase.CouchbaseProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;
import static com.gepardec.hogarama.service.couchbase.CouchbaseProducer.BUCKET_NAME;
import static com.gepardec.hogarama.service.couchbase.CouchbaseProducer.SCOPE_NAME;

@ApplicationScoped // TODO @ESI: Warum ist die WateringDAOImpl Request-Scoped?
public class WateringDataCouchbaseDAOImpl implements WateringDAO {

  @Inject
  private CouchbaseProducer couchbase;

  private static final String COLLECTION_NAME = "wateringData";

  @Override
  public List<WateringData> getWateringData(Integer maxNumber, String actorName, Date from, Date to) {
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
    );
    // @formatter:on

    try {
      return couchbase.getScope().query(statement, options).rowsAs(WateringData.class);
    } catch (QueryException e) {
      throw new TechnicalException("Query Failed: " + statement, e);
    }
  }
}
