package com.gepardec.hogarama.rest.unitmanagement.dto;

public class ActorDto extends BaseDto {

    private String name;
    private String deviceId;
    private Long unitId;
    private String queueName;

    public ActorDto() {
    }

    private ActorDto(Long id, String name, String deviceId, Long unitId, String queueName) {
        super(id);
        this.name = name;
        this.deviceId = deviceId;
        this.unitId = unitId;
        this.queueName = queueName;
    }

    public static ActorDto of(Long id, String name, String deviceId, Long unitId, String queueName) {
        return new ActorDto(id, name, deviceId, unitId, queueName);
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

    public String getQueueName() { return queueName; }

    public void setQueueName(String queueName) { this.queueName = queueName; }
}
