package com.gepardec.hogarama.domain.sensor;

import com.gepardec.hogarama.domain.watering.WateringData;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object interface for sensor data.
 *
 * <p>
 * This interface defines the contract for components that handle persistence and retrieval
 * of sensor readings and actor events. It allows different implementations (MongoDB, PostgreSQL, etc.)
 * to be used for storing time-series data from sensors and actions performed by actors.
 * </p>
 */
public interface SensorDataDAO {

    /**
     * Retrieves sensor data based on specified criteria.
     *
     * <p>
     * This method allows querying sensor readings with various filters including time range
     * and specific sensor.
     * </p>
     *
     * @param maxNumber  The maximum number of readings to return, or null for no limit
     * @param sensorName The name of the sensor to filter by, or null for all sensors
     * @param from       The start date for readings, or null for no start date
     * @param to         The end date for readings, or null for no end date
     * @return A list of SensorData objects matching the criteria
     */
    List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to);

    /**
     * Persists a sensor reading to the data store.
     *
     * <p>
     * This method saves a single sensor reading with its associated metadata
     * (sensor name, timestamp, value, etc.).
     * </p>
     *
     * @param sensorData The sensor reading to save
     */
    void save(SensorData sensorData);

    /**
     * Records an action taken by an actor.
     *
     * <p>
     * This method saves data about events when actors are activated, typically
     * in response to sensor readings (e.g., when a watering pump is turned on).
     * </p>
     *
     * @param data The data about the actor event
     */
    void saveActorEvent(WateringData data);
}
