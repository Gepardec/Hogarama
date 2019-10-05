package com.gepardec.hogarama.domain.watering;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import com.gepardec.hogarama.dao.DummySensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.watering.WateringStrategy;
import com.gepardec.hogarama.testdata.TestDataProducer;

public class WateringServiceTest {

	private WateringService watering;
    private MockActorService actor;
    private InMemoryWateringConfigDAO wateringConfigDao;

	@Before
	public void setUp() throws Exception {		
	}

	private void setupWatering(MockActorService actor, InMemoryWateringConfigDAO wateringConfigDao) {
		TestDataProducer data = new TestDataProducer(startSensorData());
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueAt(0.6, LocalDateTime.of(2019, Month.JUNE, 20, 14, 00));

		DummySensorDAO sensorDao = new DummySensorDAO(data.getData());

		watering = new WateringService(sensorDao, actor, new WateringStrategy(sensorDao), wateringConfigDao);
		watering.setDate(LocalDateTime.of(2018, Month.JUNE, 20, 15, 00));
		
	}
    
    private void setupWatering() {
        actor = new MockActorService("Vienna", "My Plant", WateringService.Config.DEFAULT.waterDuration);
        wateringConfigDao = new InMemoryWateringConfigDAO();
        setupWatering(actor, wateringConfigDao);
    }
    
	@Test
	public void testWateringOfMyPlant() throws Exception {
		setupWatering();
		watering.waterAll();
		assertTrue("Actor was called", actor.wasCalled());
	}
	
	@Test
	public void testChangeDefaultConfiguration() throws Exception {
		
		InMemoryWateringConfigDAO wateringConfigDao = new InMemoryWateringConfigDAO();
		WateringConfigData wconfig = new WateringConfigData("My Plant", "My Plant", 60, 0.2, 6);
		wateringConfigDao.save(wconfig);
		MockActorService actor = new MockActorService("Vienna", "My Plant", 6);
		
		setupWatering(actor, wateringConfigDao);
		watering.waterAll();
		assertTrue("Actor was called", actor.wasCalled());
	}
	
	@Test
	public void testWateringWillSaveDefaultConfig() throws Exception {
		setupWatering();
		watering.waterAll();

		assertNotNull(wateringConfigDao.getBySensorName("My Plant"));
	}
	
	private SensorData startSensorData() {
		return new SensorData(
				"1", 
				toDate(LocalDateTime.of(2018, Month.JUNE, 20, 15, 00)), 
				"My Plant", 
				"noramlised", 
				0.1, 
				"Vienna", 
				"1.0");
	}
	private class MockActorService implements ActorService {

		private String location;
		private String sensorName;
		private Integer duration;
		private boolean wasCalled = false;

		public MockActorService(String location, String sensorName, Integer duration) {
			this.location = location;
			this.sensorName = sensorName;
			this.duration = duration;
		}

		public boolean wasCalled() {
			return wasCalled ;
		}

		@Override
		public void sendActorMessage(String location, String sensorName, Integer duration) {
			assertEquals(this.location, location);
			assertEquals(this.sensorName, sensorName);
			assertEquals(this.duration, duration);			
			wasCalled = true;
		}

	}
}

