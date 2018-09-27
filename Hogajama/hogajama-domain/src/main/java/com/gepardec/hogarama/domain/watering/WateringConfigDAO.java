package com.gepardec.hogarama.domain.watering;

public interface WateringConfigDAO {

	void save(WateringConfigData wconf);

	WateringConfigData getBySensorName(String id);

}