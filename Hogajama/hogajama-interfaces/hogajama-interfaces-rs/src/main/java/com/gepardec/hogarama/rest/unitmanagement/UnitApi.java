package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API interface for managing unit resources.
 *
 * <p>
 * This interface defines the contract for CRUD operations on units within the Hogarama system.
 * Units represent physical or logical groupings of sensors and actors, typically corresponding to
 * a specific location or plant that is being monitored and controlled (such as a garden section,
 * individual plant, or room).
 * </p>
 *
 * <p>
 * All endpoints consume and produce JSON data and require user authentication.
 * Operations are restricted to units owned by the authenticated user.
 * </p>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UnitApi {

    /**
     * Retrieves all units owned by the authenticated user.
     *
     * @return A Response containing a list of UnitDto objects representing the user's units
     */
    @GET
    Response getForUser();

    /**
     * Creates a new unit owned by the authenticated user.
     *
     * @param unitDto The unit data transfer object containing the information for the new unit
     * @return A Response indicating success or failure of the operation
     */
    @PUT
    Response create(UnitDto unitDto);

    /**
     * Updates an existing unit with the provided information.
     *
     * @param id      The unique identifier of the unit to update
     * @param unitDto The unit data transfer object containing the updated information
     * @return A Response indicating success or failure of the operation
     */
    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, UnitDto unitDto);

    /**
     * Deletes a unit with the specified identifier.
     *
     * @param id The unique identifier of the unit to delete
     * @return A Response indicating success or failure of the operation
     */
    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
