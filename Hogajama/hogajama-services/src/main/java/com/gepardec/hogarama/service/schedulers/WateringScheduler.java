package com.gepardec.hogarama.service.schedulers;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.gepardec.hogarama.domain.watering.WateringService;
import com.gepardec.hogarama.domain.watering.WateringStrategy;

@Startup
@Singleton
public class WateringScheduler {

	@Inject
	WateringService wateringStrategy;

	@Inject
	private Logger log;

	@Schedule(hour = "*", minute = "*", second = "10", info = "Every minute", persistent = false)
	public void water() {
		log.info("Check for watering");
		wateringStrategy.waterAll();
	}
}
