package com.gepardec.hogarama.rest.v2.dto;

public class UnitDto extends BaseDTO {

    private String description;
    private Boolean isDefault;
    private String name;
    private Long ownerId;

    public UnitDto() {
    }

    private UnitDto(Long id, String description, Boolean isDefault, String name, Long ownerId) {
        super(id);
        this.description = description;
        this.isDefault = isDefault;
        this.name = name;
        this.ownerId = ownerId;
    }

    public static UnitDto of(Long id, String description, Boolean isDefault, String name, Long ownerId) {
        return new UnitDto(id, description, isDefault, name, ownerId);
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDefault() {
        return isDefault;
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

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
