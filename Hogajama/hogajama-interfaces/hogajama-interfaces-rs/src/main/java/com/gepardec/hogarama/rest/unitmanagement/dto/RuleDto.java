package com.gepardec.hogarama.rest.unitmanagement.dto;

public class RuleDto extends BaseDto {

    private String name;
    private Long sensorId;
    private Long actorId;
    private Long unitId;
    private int waterDuration;
    private double lowWater;

    public RuleDto() {
    }

    private RuleDto(Long id, String name, Long sensorId, Long actorId, Long unitId, int waterDuration, double lowWater) {
        super(id);
        this.name = name;
        this.sensorId = sensorId;
        this.actorId = actorId;
        this.unitId = unitId;
        this.waterDuration = waterDuration;
        this.lowWater = lowWater;
    }

    public static RuleDto of(Long id, String name, Long sensorId, Long actorId, Long unitId, int waterDuration, double lowWater) {
        return new RuleDto( id, name, sensorId, actorId, unitId, waterDuration, lowWater);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public int getWaterDuration() {
        return waterDuration;
    }

    public void setWaterDuration(int waterDuration) {
        this.waterDuration = waterDuration;
    }

    public double getLowWater() {
        return lowWater;
    }

    public void setLowWater(double lowWater) {
        this.lowWater = lowWater;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

}
