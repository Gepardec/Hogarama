package com.gepardec.hogarama.rest.v2.dto;

public class ActorDto extends BaseDTO {

    private String name;
    private String deviceId;
    private Long unitId;

    private ActorDto(Long id, String name, String deviceId, Long unitId) {
        super(id);
        this.name = name;
        this.deviceId = deviceId;
        this.unitId = unitId;
    }

    public static ActorDto of(Long id, String name, String deviceId, Long unitId) {
        return new ActorDto(id, name, deviceId, unitId);
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
}
