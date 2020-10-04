package com.gepardec.hogarama.domain.watering;

public interface WateringRuleDAO {

	void save(WateringRule wconf);

	WateringRule getBySensorName(String id);

    WateringRule createWateringRule(String sensorName, String actorName, double lowWater, int waterDuration);

}