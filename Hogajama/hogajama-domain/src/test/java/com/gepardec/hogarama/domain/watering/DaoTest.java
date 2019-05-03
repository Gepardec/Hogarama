package com.gepardec.hogarama.domain.watering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.gepardec.hogarama.domain.watering.WateringConfigData;

public class DaoTest  {

	private InMemoryWateringConfigDAO dao = new InMemoryWateringConfigDAO();

	@Before
	public void setUp() throws Exception {
		dao.setUpForTest();
	}

	@Test
	public void test() {
		WateringConfigData wconf = new WateringConfigData("sensor", "actor", 60, 0.2, 5);
		dao.save(wconf);
		
		String id = "sensor";
		WateringConfigData c1 = dao.getBySensorName(id);
		assertEquals("sensor", c1.getSensorName());
		assertEquals("actor", c1.getActorName());
		
		assertNull(dao.getBySensorName("sensor1"));
	}


}
