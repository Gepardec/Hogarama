package com.gepardec.hogarama.domain.entity.rule;

import com.gepardec.hogarama.domain.entity.Sensor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents the following rule:
 * 1 Sensor
 * If the <moistureThreshold> is below the measured moisture value
 * notify the owner
 */
@Entity
@Table(name = "single_sensor_rule")
public class SingleSensorRule extends Rule implements Serializable {

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "moisture_threshold", nullable = false)
    private BigDecimal moistureThreshold;

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public BigDecimal getMoistureThreshold() {
        return moistureThreshold;
    }

    public void setMoistureThreshold(BigDecimal moistureThreshold) {
        this.moistureThreshold = moistureThreshold;
    }
}
