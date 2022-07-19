package com.gepardec.hogarama.rest.unitmanagement.dto;

public class UnitDto extends BaseDto {

    private String description;
    private Boolean defaultUnit;
    private String name;
    private Long userId;

    public UnitDto() {
    }

    private UnitDto(Long id, String description, boolean defaultUnit, String name, Long userId) {
        super(id);
        this.description = description;
        this.defaultUnit = defaultUnit;
        this.name = name;
        this.userId = userId;
    }

    public static UnitDto of(Long id, String description, boolean defaultUnit, String name, Long userId) {
        return new UnitDto(id, description, defaultUnit, name, userId);
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

    public Long getUserId() {
        return userId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UnitDto{" +
                "id=" + id  +
                ", description='" + description + '\'' +
                ", defaultUnit=" + defaultUnit +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
