package com.gepardec.hogarama.domain.entity.rule;

import com.gepardec.hogarama.domain.entity.Actor;
import com.gepardec.hogarama.domain.entity.Sensor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents the following rule:
 * 1 Actor, 1 Sensor
 * If the <moistureThreshold> is below the measured moisture value
 * water for the given number of <wateringDurationInSeconds>
 */
@Entity
@Table(name = "single_sensor_actor_rule")
public class SingleSensorActorRule extends Rule {

    @Column(name = "moisture_threshold")
    private BigDecimal moistureThreshold;

    @Column(name = "watering_duration_in_seconds")
    private Integer wateringDurationInSeconds;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    public BigDecimal getMoistureThreshold() {
        return moistureThreshold;
    }

    public void setMoistureThreshold(BigDecimal moistureThreshold) {
        this.moistureThreshold = moistureThreshold;
    }

    public Integer getWateringDurationInSeconds() {
        return wateringDurationInSeconds;
    }

    public void setWateringDurationInSeconds(Integer wateringDurationInSeconds) {
        this.wateringDurationInSeconds = wateringDurationInSeconds;
    }

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
}
