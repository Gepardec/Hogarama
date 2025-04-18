package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.ActorDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API interface for managing actor resources.
 *
 * <p>
 * This interface defines the contract for CRUD operations on actors within the Hogarama system.
 * Actors represent physical devices or components that can perform actions based on sensor data
 * and rule evaluation (such as water pumps, LEDs, motors, etc.).
 * </p>
 *
 * <p>
 * All endpoints consume and produce JSON data and require user authentication.
 * Operations are restricted to actors owned by the authenticated user.
 * </p>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ActorApi {

    /**
     * Retrieves all actors owned by the authenticated user.
     *
     * @return A Response containing a list of ActorDto objects representing the user's actors
     */
    @GET
    Response getForUser();

    /**
     * Creates a new actor owned by the authenticated user.
     *
     * @param actorDto The actor data transfer object containing the information for the new actor
     * @return A Response indicating success or failure of the operation
     */
    @PUT
    Response create(ActorDto actorDto);

    /**
     * Updates an existing actor with the provided information.
     *
     * @param id       The unique identifier of the actor to update
     * @param actorDto The actor data transfer object containing the updated information
     * @return A Response indicating success or failure of the operation
     */
    @PATCH
    @Path("/{id}")
    Response update(@PathParam("id") String id, ActorDto actorDto);

    /**
     * Deletes an actor with the specified identifier.
     *
     * @param id The unique identifier of the actor to delete
     * @return A Response indicating success or failure of the operation
     */
    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);
}
