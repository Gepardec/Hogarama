package com.gepardec.hogarama.dao.dummydata;

import java.util.Date;
import java.util.function.Predicate;

import com.gepardec.hogarama.domain.sensor.SensorData;

public class SensorDataPredicate implements Predicate<SensorData> {
	
	private String sensorName;
	private Date from;
	private Date to;
	
	public SensorDataPredicate(String sensorName, Date from, Date to) {
		super();
		this.sensorName = sensorName;
		this.from = from;
		this.to = to == null ? new Date() : to;
	}

	@Override
	public boolean test(SensorData t) {
		if(t == null || !t.getSensorName().equals(this.sensorName)) {
			return false;
		}
		
		if (from != null) {
			long sensorTimeInLong = t.getTime().getTime();
			return sensorTimeInLong >= from.getTime() && sensorTimeInLong <= to.getTime();
		}
		
		return true;
	}
}
