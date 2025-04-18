package com.gepardec.hogarama.rest.unitmanagement;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API interface for user information management.
 *
 * <p>
 * This interface defines the endpoints for retrieving information about the
 * currently authenticated user within the Hogarama system. Unlike other APIs,
 * this interface only provides read access to user information, as user creation
 * and management is typically handled through an external identity provider.
 * </p>
 *
 * <p>
 * All endpoints consume and produce JSON data and require user authentication.
 * </p>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserApi {

    /**
     * Retrieves information about the currently authenticated user.
     *
     * @return A Response containing information about the current user
     */
    @GET
    Response getUser();

}
