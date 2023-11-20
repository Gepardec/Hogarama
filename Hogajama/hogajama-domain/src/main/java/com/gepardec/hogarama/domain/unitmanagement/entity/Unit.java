package com.gepardec.hogarama.domain.unitmanagement.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UnitIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "UnitIdGenerator", sequenceName = "seq_unit_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;

    private String description;

    @Column(columnDefinition = "boolean default false", name = "is_default", nullable = false)
    private Boolean defaultUnit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "unit")
    private List<Sensor> sensorList;

    @OneToMany(mappedBy = "unit")
    private List<Actor> actorList;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    public Boolean getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(Boolean defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public Boolean isDefaultUnit() {
        return defaultUnit;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
    }

    public static Unit createDefault(User user) {
        Unit unit = new Unit();
        unit.setDefaultUnit(true);
        unit.setName("DEFAULT_UNIT");
        unit.setDescription("Automatically created default unit.");
        unit.setUser(user);
        return unit;
    }

}
