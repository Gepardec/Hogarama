package com.gepardec.hogarama.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.gepardec.hogarama.domain.watering.WateringConfigData;
import com.gepardec.hogarama.domain.watering.WateringRule;
import com.gepardec.hogarama.service.dao.MongoWateringRuleDAO;

public class DaoTest  {

	private MongoWateringRuleDAO dao = new MongoWateringRuleDAO();

	@Before
	public void setUp() throws Exception {
		dao.setUpForTest();
	}

	@Test @Ignore // Das ist eher ein Integrationstest. Man benötigt Mongo-Zugriff
	public void test() {
		WateringConfigData wconf = new WateringConfigData("sensor", "actor", 0.2, 5);
		dao.save(wconf);
		
		String id = "sensor";
		WateringRule c1 = dao.getBySensorName(id);
		assertEquals("sensor", c1.getSensorName());
		assertEquals("actor", c1.getActorName());
		
		assertNull(dao.getBySensorName("sensor1"));
	}


}
