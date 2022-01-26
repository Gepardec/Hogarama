package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.annotations.CouchbaseDAO;
import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.watering.WateringDataDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class DAOProducer {

  private static final Logger LOG = LoggerFactory.getLogger(DAOProducer.class);

  @Inject @CouchbaseDAO
  private CouchbaseSensorDataDAO couchbaseSensorDataDAO;

  @Inject @CouchbaseDAO
  private CouchbaseWateringDataDAO couchbaseWateringDataDAO;

  @Inject @MongoDAO
  private MongoSensorDataDAO mongoSensorDataDAO;

  @Inject @MongoDAO
  private MongoWateringDataDAO mongoWateringDataDAO;

  @Produces
  public SensorDataDAO getSensorDataDAO() {
    if (System.getProperty("hogarama.sensorData.storage", "mongo").contentEquals("couchbase")) {
      LOG.debug("Produce CouchbaseSensorDataDAO");
      return couchbaseSensorDataDAO;
    }
    LOG.debug("Produce CouchbaseSensorDataDAO");
    return mongoSensorDataDAO;
  }

  @Produces
  public WateringDataDAO getWateringDataDAO() {
    if (System.getProperty("hogarama.wateringData.storage", "mongo").contentEquals("couchbase")) {
      LOG.debug("Produce CouchbaseWateringDataDAO");
      return couchbaseWateringDataDAO;
    }
    LOG.debug("Produce CouchbaseWateringDataDAO");
    return mongoWateringDataDAO;
  }

}
