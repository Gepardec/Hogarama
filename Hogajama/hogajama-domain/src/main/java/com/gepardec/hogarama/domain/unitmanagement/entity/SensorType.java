package com.gepardec.hogarama.domain.unitmanagement.entity;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.gepardec.hogarama.domain.sensor.MappingType;

@Entity
@Table(name = "sensor_type")
public class SensorType implements Serializable{

    private static final long serialVersionUID = 1L;

    public SensorType() {
    }
    
    public SensorType(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    @Id
    @GeneratedValue(generator = "SensorTypeGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SensorTypeGenerator", sequenceName = "seq_sensor_type_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @SuppressWarnings("serial")
    private static final HashMap<String, MappingType> SENSORS = new HashMap<String, MappingType>() {{
        put("IDENTITY", MappingType.IDENTITY);
        put("LINEAR100", MappingType.LINEAR100);
        put("LINEAR1024", MappingType.LINEAR1024);
        put("INVERSE_LINEAR1024", MappingType.INVERSE_LINEAR1024);
        put("Chinese Water Sensor", MappingType.INVERSE_LINEAR1024);
        put("sparkfun", MappingType.LINEAR1024);
    }};

    public static MappingType getMappingType(String type) {
        return SENSORS.getOrDefault(type, MappingType.LINEAR100);
    }

    public MappingType getMappingType() {
        return getMappingType(name);
    }

}
