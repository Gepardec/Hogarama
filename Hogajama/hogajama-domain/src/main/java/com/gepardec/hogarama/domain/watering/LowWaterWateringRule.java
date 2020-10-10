package com.gepardec.hogarama.domain.watering;

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
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;


@Entity
@Table(name = "wateringrule")
public class LowWaterWateringRule implements WateringRule, Serializable {

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

	public LowWaterWateringRule() {
		
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
