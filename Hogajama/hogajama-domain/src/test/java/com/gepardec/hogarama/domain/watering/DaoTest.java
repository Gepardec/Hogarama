package com.gepardec.hogarama.domain.watering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DaoTest  {

	private InMemoryWateringConfigDAO dao = new InMemoryWateringConfigDAO();

	@BeforeEach
	public void setUp() throws Exception {
		dao.setUpForTest();
	}

	@Test
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
