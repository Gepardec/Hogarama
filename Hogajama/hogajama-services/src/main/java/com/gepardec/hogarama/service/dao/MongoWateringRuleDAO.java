package com.gepardec.hogarama.service.dao;

import java.util.List;

import javax.inject.Inject;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.watering.WateringRuleDAO;
import com.gepardec.hogarama.annotations.MongoDAO;
import com.gepardec.hogarama.domain.watering.WateringConfigData;
import com.gepardec.hogarama.domain.watering.WateringRule;
import com.gepardec.hogarama.service.MongoDbProducer;

@MongoDAO
public class MongoWateringRuleDAO implements WateringRuleDAO {

  private static final Logger LOG = LoggerFactory.getLogger(MongoWateringRuleDAO.class);

  @Inject
  public Datastore db;

  public MongoWateringRuleDAO() {
  }

  @Override
  public void save(WateringRule wconf) {
    db.save(wconf);
  }

  @Override
  public WateringRule getBySensorName(String sensorName) {
    LOG.debug("getBySensorName " + sensorName);
    List<WateringConfigData> configs = db.createQuery(WateringConfigData.class).field("sensorName").equal(sensorName).asList();
    if (configs.isEmpty()) {
      return null;
    }
    return configs.get(0);
  }

  @Override
  public WateringRule createWateringRule(String sensorName, String actorName, double lowWater, int waterDuration) {
    return new WateringConfigData(sensorName, actorName, lowWater, waterDuration);
  }

  public void setUpForTest() {
    MongoDbProducer producer = new MongoDbProducer();
    db = producer.getDatastore();
  }

}
