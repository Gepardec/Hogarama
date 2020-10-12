package com.gepardec.hogarama.domain.watering;

public interface WateringRule {

    String getSensorName();

    String getActorName();

    double getLowWater();

    int getWaterDuration();

}