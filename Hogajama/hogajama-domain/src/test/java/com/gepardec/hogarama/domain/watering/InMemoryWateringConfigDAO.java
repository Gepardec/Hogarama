package com.gepardec.hogarama.domain.watering;

import java.util.HashMap;

public class InMemoryWateringConfigDAO implements WateringConfigDAO {

	private HashMap<String, WateringConfigData> store = new HashMap<>();

	@Override
	public void save(WateringConfigData wconf) {
		store.put(wconf.getSensorName(), wconf);
	}

	@Override
	public WateringConfigData getBySensorName(String id) {
		return store.get(id);
	}

	public void setUpForTest() {
		// TODO Auto-generated method stub
		
	}

}
