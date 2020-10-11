package com.gepardec.hogarama.domain.unitmanagement.entity;

public class EntityNotFoundException  extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Long sensorId, Class<?> clazz){
        super(String.format("Can't find entity of type %s with id %d!", clazz, sensorId));
    }
}
