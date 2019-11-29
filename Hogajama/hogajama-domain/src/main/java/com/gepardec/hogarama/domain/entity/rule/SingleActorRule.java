package com.gepardec.hogarama.domain.entity.rule;

import com.gepardec.hogarama.domain.entity.Actor;

import javax.persistence.*;

@Entity
@Table(name = "single_actor_rule")
public class SingleActorRule extends Rule {

    @Column(name = "watering_duration_in_seconds", nullable = false)
    private Integer wateringDurationInSeconds;
    @Column(name = "interval_between_waterings_in_hours", nullable = false)
    private Integer intervalBetweenWateringsInHours;

    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    public Integer getWateringDurationInSeconds() {
        return wateringDurationInSeconds;
    }

    public void setWateringDurationInSeconds(Integer wateringDurationInSeconds) {
        this.wateringDurationInSeconds = wateringDurationInSeconds;
    }

    public Integer getIntervalBetweenWateringsInHours() {
        return intervalBetweenWateringsInHours;
    }

    public void setIntervalBetweenWateringsInHours(Integer intervalBetweenWateringsInHours) {
        this.intervalBetweenWateringsInHours = intervalBetweenWateringsInHours;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
