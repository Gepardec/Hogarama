package com.gepardec.hogarama.domain;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

		watering.setHabaramaDAO(new DummySensorDAO(data.getData()));
	}

	@Test
	public void whenEmptyListNoWatering() {
		assertFalse(watering.water("My Plant", LocalDateTime.of(2018, Month.JUNE, 20, 17, 00)));
	}

	@Test
	public void whenLastDataHighThenNoWatering() throws Exception {		
		assertFalse(watering.water("My Plant", LocalDateTime.of(2018, Month.JUNE, 20, 14, 00)));
	}

	@Test
	public void whenLastDataLowThenWatering() throws Exception {		
		assertTrue(watering.water("My Plant", LocalDateTime.of(2018, Month.JUNE, 20, 15, 00)));
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
}
