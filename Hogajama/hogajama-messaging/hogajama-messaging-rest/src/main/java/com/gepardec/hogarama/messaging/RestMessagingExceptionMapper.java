package com.gepardec.hogarama.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class RestMessagingExceptionMapper implements ExceptionMapper<RestMessagingException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMessagingExceptionMapper.class);

    @Override
    public Response toResponse(RestMessagingException e) {
        UUID uuid = UUID.randomUUID();
        LOGGER.error("Unexpected exception occurred {}", uuid, e);
        String responseMessage = String.format("Unexpected exception occurred (ref %s)", uuid);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseMessage).build();
    }
}
