package com.gepardec.hogarama.domain.unitmanagement.entity;

import java.io.Serializable;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gepardec.hogarama.domain.unitmanagement.cache.ActorCache;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.watering.WateringRule;


@Entity
@Table(name = "wateringrule")
public class Rule implements WateringRule, Serializable, Owned {

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

    public void setId(Long id) {
        this.id = id;        
    }   
    public void setWaterDuration(int waterDuration) {
        this.waterDuration = waterDuration;
    }

    public void setLowWater(double lowWater) {
        this.lowWater = lowWater;
    }


	public Rule() {
		this.isValid = true;
	}
	
    public Rule(Sensor sensor, Actor actor, Unit unit, String name, double lowWater, int waterDuration) {
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
