package com.gepardec.hogarama.domain.watering;

import com.gepardec.hogarama.dao.DummySensorDataDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.testdata.TestDataProducer;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static org.junit.Assert.*;

public class WateringServiceTest {

	private WateringService watering;
    private MockActorService actorSvc;
    private InMemoryWateringConfigDAO wateringConfigDao;
    private TestDataProducer data;

	@Before
	public void setUp() throws Exception {		
	}

	private void setupWatering(MockActorService actorSvc, InMemoryWateringConfigDAO wateringConfigDao) {
		data = new TestDataProducer(startSensorData());
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueAt(0.6, LocalDateTime.of(2019, Month.JUNE, 20, 14, 00));

		DummySensorDataDAO sensorDataDAO = new DummySensorDataDAO(data.getData());

		watering = new WateringService(sensorDataDAO, actorSvc, new WateringStrategy(sensorDataDAO), wateringConfigDao);
		
	}
    
    private void setupWatering() {
        actorSvc = new MockActorService("Vienna", "My Plant", WateringService.Config.DEFAULT.waterDuration);
        wateringConfigDao = new InMemoryWateringConfigDAO();
        setupWatering(actorSvc, wateringConfigDao);
    }
    
	@Test
	public void testWateringOfMyPlant() throws Exception {
		setupWatering();
		waterAll();
		assertTrue("Actor was called", actorSvc.wasCalled());
	}
	
    @Test
	public void testChangeDefaultConfiguration() throws Exception {
		
		InMemoryWateringConfigDAO wateringConfigDao = new InMemoryWateringConfigDAO();
		WateringConfigData wconfig = new WateringConfigData("My Plant", "My Plant", 60, 0.2, 6);
		wateringConfigDao.save(wconfig);
		MockActorService actor = new MockActorService("Vienna", "My Plant", 6);
		
		setupWatering(actor, wateringConfigDao);
		waterAll();
		assertTrue("Actor was called", actor.wasCalled());
	}

   @Test
    public void testUseDifferentActorNameThanSensorName() throws Exception {
        
        InMemoryWateringConfigDAO wateringConfigDao = new InMemoryWateringConfigDAO();
        WateringConfigData wconfig = new WateringConfigData("My Plant", "My Actor", 60, 0.2, 6);
        wateringConfigDao.save(wconfig);
        MockActorService actor = new MockActorService("Vienna", "My Actor", 6);
        
        setupWatering(actor, wateringConfigDao);
        waterAll();
        assertTrue("Actor was called", actor.wasCalled());
    }

	@Test
	public void testWateringWillSaveDefaultConfig() throws Exception {
		setupWatering();
		waterAll();

		assertNotNull(wateringConfigDao.getBySensorName("My Plant"));
	}
	
    @Test
    public void testLowValueWillTriggerWatering() throws Exception {
        setupWatering();
        SensorData val = data.getNext();
        assertEquals(0.1, val.getValue(), 0.01);
        watering.water(val);
        assertTrue("Actor was called", actorSvc.wasCalled());
   }
    
    @Test
    public void testHighValueWontTriggerWatering() throws Exception {
        setupWatering();
        data.getNext();
        data.getNext();
        data.getNext();
        SensorData val = data.getNext();
        assertEquals(0.6, val.getValue(), 0.01);
        watering.water(val);
        assertFalse("Actor was not called", actorSvc.wasCalled());
   }
    
    @Test
    public void testStrategieUsesAverage() throws Exception {
        setupWatering();
        watering.water(data.getNext().setValue(0.4));
        assertFalse("Actor was not called with 0.4", actorSvc.wasCalled());
        watering.water(data.getNext().setValue(0.1));
        assertFalse("Actor was not called with 0.1", actorSvc.wasCalled());
        watering.water(data.getNext().setValue(0.1));
        assertFalse("Actor was not called with second 0.1", actorSvc.wasCalled());
        watering.water(data.getNext().setValue(0.1));
        assertTrue("Actor was called with third 0.1", actorSvc.wasCalled());
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
	
	private void waterAll() {
	    data.waterAll(watering);
	}

	private class MockActorService implements ActorService {

		private String location;
		private String actorName;
		private Integer duration;
		private boolean wasCalled = false;

		public MockActorService(String location, String actorName, Integer duration) {
			this.location = location;
			this.actorName = actorName;
			this.duration = duration;
		}

		public boolean wasCalled() {
			return wasCalled ;
		}

		@Override
		public void sendActorMessage(String location, String actorName, Integer duration) {
			assertEquals(this.location, location);
			assertEquals(this.actorName, actorName);
			assertEquals(this.duration, duration);			
			wasCalled = true;
		}

	}
}

