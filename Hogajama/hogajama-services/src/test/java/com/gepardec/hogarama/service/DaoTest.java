package com.gepardec.hogarama.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.gepardec.hogarama.domain.watering.WateringConfigData;
import com.gepardec.hogarama.service.dao.MongoWateringConfigDAO;

public class DaoTest  {

	private MongoWateringConfigDAO dao = new MongoWateringConfigDAO();

	@Before
	public void setUp() throws Exception {
		dao.setUpForTest();
	}

	@Test @Ignore // Das ist eher ein Integrationstest. Man benötigt Mongo-Zugriff
	public void test() {
		WateringConfigData wconf = new WateringConfigData("sensor", "actor", 0.2, 5);
		dao.save(wconf);
		
		String id = "sensor";
		WateringConfigData c1 = dao.getBySensorName(id);
		assertEquals("sensor", c1.getSensorName());
		assertEquals("actor", c1.getActorName());
		
		assertNull(dao.getBySensorName("sensor1"));
	}


}
