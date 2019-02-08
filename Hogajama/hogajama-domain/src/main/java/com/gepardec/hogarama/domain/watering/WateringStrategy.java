package com.gepardec.hogarama.domain.watering;

import static com.gepardec.hogarama.domain.DateUtils.toDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;

public class WateringStrategy {
 
	@Inject
	private SensorDAO sensorDao;
	
	public WateringStrategy() {
	}

	
	protected WateringStrategy(SensorDAO dao) {
		this.sensorDao = dao;
	}


	public int water(WateringConfigData config, LocalDateTime now) {
		List<SensorData> data = sensorDao.getAllData(200, config.getSensorName(), toDate(now.minus(Duration.ofMinutes(config.getMeasureInterval()))), toDate(now.plus(Duration.ofSeconds(1))));
		double sum = 0;
		for (SensorData sensorData : data) {
			sum += sensorData.getValue();
		}
		double avg = sum / data.size();
		if ( avg < config.getLowWater() ) {
			return config.getWaterDuration();
		}
		else return 0;
	}
	
}
