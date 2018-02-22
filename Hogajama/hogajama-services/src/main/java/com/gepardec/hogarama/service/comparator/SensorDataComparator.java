package com.gepardec.hogarama.service.comparator;

import java.util.Comparator;

import com.gepardec.hogarama.domain.SensorData;

public class SensorDataComparator implements Comparator<SensorData>{

	@Override
	public int compare(SensorData o1, SensorData o2) {
		return o1.getTime().compareTo(o2.getTime());
	}

}
