package com.gepardec.hogarama.service.dao;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.annotations.PostgresDAO;
import com.gepardec.hogarama.domain.unitmanagement.cache.ActorCache;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.dao.BaseDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.watering.LowWaterWateringRule;
import com.gepardec.hogarama.domain.watering.WateringRule;
import com.gepardec.hogarama.domain.watering.WateringRuleDAO;

@PostgresDAO
public class PostgresWateringRuleDAO extends BaseDAO<LowWaterWateringRule> implements WateringRuleDAO{
	
    private static final Logger LOG = LoggerFactory.getLogger(PostgresWateringRuleDAO.class);

    @Inject
    private ActorCache actorCache;

    @Inject
    private SensorCache sensorCache;

	public PostgresWateringRuleDAO() {
	}
	
	@Override
	public void save(WateringRule rule) {
	    LowWaterWateringRule r = (LowWaterWateringRule)rule;
	    if ( !r.isValid() ) {
		    LOG.warn("Watering rule is not valid. Probably sensor or actor doesn't exist in database. Won't persist rule to database!");
		    return;
	    }
	    super.save(r);
	}

	@Override
	public WateringRule getBySensorName(String sensorName) {
        LOG.debug("getBySensorName " + sensorName);
		List<LowWaterWateringRule> configs = 
		        entityManager.createQuery("from LowWaterWateringRule r where r.sensor.deviceId=:sensorName")
		        .setParameter("sensorName", sensorName)
		        .getResultList();
		if (configs.isEmpty()){
			return null;
		}
		return configs.get(0);
	}

    @Override
    public WateringRule createWateringRule(String sensorName, String actorName, double lowWater, int waterDuration) {
        Sensor sensor = getSensor(sensorName);
        return new LowWaterWateringRule(sensor, getActor(actorName), getUnit(sensor), sensorName + "_" + actorName, lowWater, waterDuration);
    }

    @Override
    public Class<LowWaterWateringRule> getEntityClass() {
        return LowWaterWateringRule.class;
    }
    
    private Sensor getSensor(String sensorName) {
        Optional<Sensor> optional = sensorCache.getByDeviceId(sensorName);
        if ( !optional.isPresent() ) {
            return null;
        }
        return optional.get();
    }

    private Actor getActor(String actorName) {
        Optional<Actor> optional = actorCache.getByDeviceId(actorName);
        if ( !optional.isPresent() ) {
            return null;
        }
        return optional.get();
    }

    private Unit getUnit(Sensor sensor) {
        if ( null != sensor ) {
            return sensor.getUnit();
        }
        return null;
    }

}