package com.gepardec.hogarama.rest.unitmanagement.dto;

public abstract class BaseDto {

    protected Long id;

    public BaseDto() {
    }

    protected BaseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
