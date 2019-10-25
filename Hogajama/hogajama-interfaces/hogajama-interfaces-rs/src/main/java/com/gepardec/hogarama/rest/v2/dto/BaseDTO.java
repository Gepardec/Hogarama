package com.gepardec.hogarama.rest.v2.dto;

public abstract class BaseDTO {

    protected Long id;


    public BaseDTO() {
    }

    protected BaseDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
