package com.gepardec.hogarama.rest.unitmanagement.dto;

public class UnitDto extends BaseDto {

    private String description;
    private Boolean defaultUnit;
    private String name;
    private Long ownerId;

    public UnitDto() {
    }

    private UnitDto(Long id, String description, boolean defaultUnit, String name, Long ownerId) {
        super(id);
        this.description = description;
        this.defaultUnit = defaultUnit;
        this.name = name;
        this.ownerId = ownerId;
    }

    public static UnitDto of(Long id, String description, boolean defaultUnit, String name, Long ownerId) {
        return new UnitDto(id, description, defaultUnit, name, ownerId);
    }

    public String getDescription() {
        return description;
    }

    public Boolean isDefaultUnit() {
        return defaultUnit;
    }

    public String getName() {
        return name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDefaultUnit(Boolean defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
