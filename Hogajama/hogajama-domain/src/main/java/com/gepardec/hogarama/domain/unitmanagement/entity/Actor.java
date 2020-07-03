package com.gepardec.hogarama.domain.unitmanagement.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Actor implements Serializable {

    @Id
    @GeneratedValue(generator = "ActorIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ActorIdGenerator", sequenceName = "seq_actor_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;

    @Column(name = "device_id", unique = true)
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "queue_name")
    private String queueName;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getQueueName() { return queueName; }

    public void setQueueName(String queueName) { this.queueName = queueName; }
}
