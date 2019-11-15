package com.gepardec.hogarama.domain.watering;

import com.gepardec.hogarama.dao.DummySensorDataDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.testdata.TestDataProducer;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WateringStrategyTest {

	private WateringStrategy watering;
	
	private WateringConfigData config;

	@Before
	public void setUp() throws Exception {
		
		TestDataProducer data = new TestDataProducer(startSensorData());
		
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueMinusMinutes( 0.1, 10);
		data.addValueAt(0.6, LocalDateTime.of(2019, Month.JUNE, 20, 14, 00));

		watering = new WateringStrategy(new DummySensorDataDAO(data.getData()));
		
		config = new WateringConfigData("My Plant", "My Plant", 60, 0.2, 5);
	}

	@Test
	public void whenEmptyListNoWatering() {
		assertFalse( 0 <  watering.water(config, LocalDateTime.of(2018, Month.JUNE, 20, 17, 00)));
	}

	@Test
	public void whenLastDataHighThenNoWatering() throws Exception {		
		assertFalse( 0 <  watering.water(config, LocalDateTime.of(2018, Month.JUNE, 20, 14, 00)));
	}

	@Test
	public void whenLastDataLowThenWatering() throws Exception {		
		assertTrue( 0 < watering.water(config, LocalDateTime.of(2018, Month.JUNE, 20, 15, 00)));
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

