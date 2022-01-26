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

  @Test
  public void testInjection_OK() {
    assertNotNull(sensorDataDAO);
    assertNotNull(wateringDataDAO);
  }

}
