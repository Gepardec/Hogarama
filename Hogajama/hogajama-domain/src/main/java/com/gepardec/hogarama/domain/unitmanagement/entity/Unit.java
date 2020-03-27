package com.gepardec.hogarama.domain.unitmanagement.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Unit {

    @Id
    @GeneratedValue(generator = "UnitIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "UnitIdGenerator", sequenceName = "seq_unit_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "unit")
    private List<Sensor> sensorList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

}
