package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API interface for managing sensor resources.
 *
 * <p>
 * This interface defines the contract for CRUD operations on sensors within the Hogarama system.
 * Sensors represent physical devices that collect data from the environment (such as soil moisture sensors,
 * temperature sensors, light sensors, etc.).
 * </p>
 *
 * <p>
 * All endpoints consume and produce JSON data and require user authentication.
 * Operations are restricted to sensors owned by the authenticated user.
 * </p>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SensorApi {

    /**
     * Retrieves all sensors owned by the authenticated user.
     *
     * @return A Response containing a list of SensorDto objects representing the user's sensors
     */
    @GET
    Response getForUser();

    /**
     * Creates a new sensor owned by the authenticated user.
     *
     * @param sensorDto The sensor data transfer object containing the information for the new sensor
     * @return A Response indicating success or failure of the operation
     */
    @PUT
    Response create(SensorDto sensorDto);

    /**
     * Updates an existing sensor with the provided information.
     *
     * @param id        The unique identifier of the sensor to update
     * @param sensorDto The sensor data transfer object containing the updated information
     * @return A Response indicating success or failure of the operation
     */
    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, SensorDto sensorDto);

    /**
     * Deletes a sensor with the specified identifier.
     *
     * @param id The unique identifier of the sensor to delete
     * @return A Response indicating success or failure of the operation
     */
    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
