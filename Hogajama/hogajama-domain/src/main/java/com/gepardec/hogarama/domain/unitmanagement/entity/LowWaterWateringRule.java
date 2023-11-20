package com.gepardec.hogarama.domain.unitmanagement.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.gepardec.hogarama.domain.watering.WateringRule;


@Entity
@Table(name = "low_water_watering_rule")
public class LowWaterWateringRule implements WateringRule, Serializable, Owned {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "WateringRuleGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "WateringRuleGenerator", sequenceName = "seq_rule_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;
    
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    
    @Column(name = "water_duration")
    private int waterDuration;
    
    @Column(name = "low_water")
    private double lowWater;

    @Transient
    private boolean isValid;
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;        
    }   
    public void setWaterDuration(int waterDuration) {
        this.waterDuration = waterDuration;
    }

    public void setLowWater(double lowWater) {
        this.lowWater = lowWater;
    }


	public LowWaterWateringRule() {
		this.isValid = true;
	}
	
    public LowWaterWateringRule(Sensor sensor, Actor actor, Unit unit, String name, double lowWater, int waterDuration) {
        this.isValid = null != sensor && null != actor && null != unit;
        this.sensor = sensor;
        this.actor = actor;
        this.unit = unit;
        this.name = name;
        this.lowWater = lowWater;
        this.waterDuration = waterDuration;
    }

    @Override
    public String getSensorName() {
        if ( isValid ) {
            return sensor.getDeviceId();
        }
		return "";
	}

	@Override
    public String getActorName() {
        if ( isValid ) {
            return actor.getDeviceId();
        }
        return "";
	}

	@Override
    public double getLowWater() {
		return lowWater;
	}	
	
	@Override
    public int getWaterDuration() {
		return waterDuration;
	}

    public boolean isValid() {
        return isValid;
    }

}
