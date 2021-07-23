package com.gepardec.hogarama.domain.watering;

import java.util.HashMap;

public class InMemoryWateringConfigDAO implements WateringRuleDAO {

	private HashMap<String, WateringRule> store = new HashMap<>();

	@Override
	public void save(WateringRule wconf) {
		store.put(wconf.getSensorName(), wconf);
	}

	@Override
	public WateringRule getBySensorName(String id) {
		return store.get(id);
	}

	public void setUpForTest() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public WateringRule createWateringRule(String sensorName, String actorName, double lowWater, int waterDuration) {
          return new WateringConfigData(sensorName, actorName, lowWater, waterDuration);
    }

}
