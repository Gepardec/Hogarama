package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API interface for managing rule resources.
 *
 * <p>
 * This interface defines the contract for CRUD operations on rules within the Hogarama system.
 * Rules define the automation logic that ties sensors and actors together, specifying conditions
 * under which actors should be triggered based on sensor readings (such as watering a plant when
 * soil moisture falls below a threshold).
 * </p>
 *
 * <p>
 * All endpoints consume and produce JSON data and require user authentication.
 * Operations are restricted to rules owned by the authenticated user.
 * </p>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RuleApi {

    /**
     * Retrieves all rules owned by the authenticated user.
     *
     * @return A Response containing a list of RuleDto objects representing the user's rules
     */
    @GET
    Response getForUser();

    /**
     * Creates a new rule owned by the authenticated user.
     *
     * @param ruleDto The rule data transfer object containing the information for the new rule
     * @return A Response indicating success or failure of the operation
     */
    @PUT
    Response create(RuleDto ruleDto);

    /**
     * Updates an existing rule with the provided information.
     *
     * @param id      The unique identifier of the rule to update
     * @param ruleDto The rule data transfer object containing the updated information
     * @return A Response indicating success or failure of the operation
     */
    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, RuleDto ruleDto);

    /**
     * Deletes a rule with the specified identifier.
     *
     * @param id The unique identifier of the rule to delete
     * @return A Response indicating success or failure of the operation
     */
    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
