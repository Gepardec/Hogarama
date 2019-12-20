package com.gepardec.hogarama.domain.entity;

import com.gepardec.hogarama.domain.entity.rule.Rule;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Unit implements Serializable {

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
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "unit")
    private List<Sensor> sensorList;

    @OneToMany(mappedBy = "unit")
    private List<Actor> actorList;

    @OneToMany(mappedBy = "unit")
    private List<Rule> ruleList;

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

    public List<Actor> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public Boolean getDefaultUnit() {
        return defaultUnit;
    }

    public Boolean isDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(Boolean defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public static Unit createDefault(Owner owner) {
        Unit unit = new Unit();
        unit.setDefaultUnit(true);
        unit.setName("DEFAULT_UNIT");
        unit.setDescription("Automatically created default unit.");
        unit.setOwner(owner);
        return unit;
    }
}
