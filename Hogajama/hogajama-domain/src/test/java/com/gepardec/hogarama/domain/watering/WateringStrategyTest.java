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

public class WateringStrategyTest {

	private WateringStrategy watering;

	@Before
	public void setUp() throws Exception {
		watering = new WateringStrategy();
		
		TestDataProducer data = new TestDataProducer(startSensorData());
		
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueAt(0.6, LocalDateTime.of(2019, Month.JUNE, 20, 14, 00));

		watering.setSensorDAO(new DummySensorDAO(data.getData()));
	}

	@Test
	public void whenEmptyListNoWatering() {
		assertFalse( 0 <  watering.water("My Plant", LocalDateTime.of(2018, Month.JUNE, 20, 17, 00)));
	}

	@Test
	public void whenLastDataHighThenNoWatering() throws Exception {		
		assertFalse( 0 <  watering.water("My Plant", LocalDateTime.of(2018, Month.JUNE, 20, 14, 00)));
	}

	@Test
	public void whenLastDataLowThenWatering() throws Exception {		
		assertTrue( 0 < watering.water("My Plant", LocalDateTime.of(2018, Month.JUNE, 20, 15, 00)));
	}
	
	@Test
	public void testWateringOfMyPlant() throws Exception {
		MockActorService actor = new MockActorService("Vienna", "My Plant", 5);
		watering.setActor(actor);
		watering.setDate(LocalDateTime.of(2018, Month.JUNE, 20, 15, 00));
		watering.waterAll();
		assertTrue("Actor was called", actor.wasCalled());
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

