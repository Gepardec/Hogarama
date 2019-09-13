package com.gepardec.hogarama.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "sensor_type")
public class SensorType {

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
}
