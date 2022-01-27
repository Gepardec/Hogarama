package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.watering.WateringDataDAO;
import com.gepardec.hogarama.service.CouchbaseProducer;
import com.gepardec.hogarama.service.MongoDbProducer;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddBeanClasses({DAOProducer.class})
@AddBeanClasses({CouchbaseSensorDataDAO.class, CouchbaseWateringDataDAO.class, CouchbaseProducer.class})
@AddBeanClasses({MongoSensorDataDAO.class, MongoWateringDataDAO.class, MongoDbProducer.class})
@AddBeanClasses({SensorNormalizer.class, SensorCache.class, SensorDAO.class})
class DAOProducerIT {

  @Inject
  private SensorDataDAO   sensorDataDAO;
  @Inject
  private WateringDataDAO wateringDataDAO;
  @Inject
  private DAOProducer     producer;

  private String sensorProperty;
  private String wateringProperty = "";

  private static final String SENSOR_PROPERTY_KEY   = "hogarama.sensorData.storage";
  private static final String WATERING_PROPERTY_KEY = "hogarama.wateringData.storage";

  @BeforeEach
  public void setup() {
    sensorProperty = System.getProperty(SENSOR_PROPERTY_KEY, "");
    wateringProperty = System.getProperty(WATERING_PROPERTY_KEY,"");
  }

  @AfterEach
  public void teardown() {
    if(!sensorProperty.isEmpty()) {
      System.setProperty(SENSOR_PROPERTY_KEY, sensorProperty);
    }
    if(!wateringProperty.isEmpty()) {
      System.setProperty(WATERING_PROPERTY_KEY, wateringProperty);
    }
  }

  @Test
  public void testSensorDataDAOInjection_OK() {
    assertNotNull(sensorDataDAO);
    if (System.getProperty(SENSOR_PROPERTY_KEY, "mongo").contentEquals("couchbase")) {
      assertInstanceOf(CouchbaseSensorDataDAO.class, sensorDataDAO);
    } else {
      assertInstanceOf(MongoSensorDataDAO.class, sensorDataDAO);
    }
  }

  @Test
  public void testWateringDataDAOInjection_OK() {
    assertNotNull(wateringDataDAO);
    if (System.getProperty(WATERING_PROPERTY_KEY, "mongo").contentEquals("couchbase")) {
      assertInstanceOf(CouchbaseWateringDataDAO.class, wateringDataDAO);
    } else {
      assertInstanceOf(MongoWateringDataDAO.class, wateringDataDAO);
    }
  }

  @Test
  public void testGetSensorDataDAO_Couchbase_OK() {
    System.setProperty(SENSOR_PROPERTY_KEY, "couchbase");
    assertInstanceOf(CouchbaseSensorDataDAO.class, producer.getSensorDataDAO());
  }

  @Test
  public void testGetSensorDataDAO_Mongo_OK() {
    System.setProperty(SENSOR_PROPERTY_KEY, "mongo");
    assertInstanceOf(MongoSensorDataDAO.class, producer.getSensorDataDAO());
  }

  @Test
  public void testGetWateringDataDAO_Couchbase_OK() {
    System.setProperty(WATERING_PROPERTY_KEY, "couchbase");
    assertInstanceOf(CouchbaseWateringDataDAO.class, producer.getWateringDataDAO());
  }

  @Test
  public void testGetWateringDataDAO_Mongo_OK() {
    System.setProperty(WATERING_PROPERTY_KEY, "mongo");
    assertInstanceOf(MongoWateringDataDAO.class, producer.getWateringDataDAO());
  }

}
