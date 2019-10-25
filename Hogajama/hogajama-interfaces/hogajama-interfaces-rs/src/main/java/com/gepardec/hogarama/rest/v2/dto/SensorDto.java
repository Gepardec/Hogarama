package com.gepardec.hogarama.rest.v2.dto;

public class SensorDto extends BaseDTO {

    private String name;
    private String deviceId;
    private Long unitId;
    private Long sensorTypeId;

    public SensorDto() {
    }

    private SensorDto(Long id, String name, String deviceId, Long unitId, Long sensorTypeId) {
        super(id);
        this.name = name;
        this.deviceId = deviceId;
        this.unitId = unitId;
        this.sensorTypeId = sensorTypeId;
    }

    public static SensorDto of(Long id, String name, String deviceId, Long unitId, Long sensorTypeId) {
        return new SensorDto(id, name, deviceId, unitId, sensorTypeId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getSensorTypeId() {
        return sensorTypeId;
    }

    public void setSensorTypeId(Long sensorTypeId) {
        this.sensorTypeId = sensorTypeId;
    }


}
