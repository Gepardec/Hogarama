package com.gepardec.hogarama.messaging;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {
        UUID uuid = UUID.randomUUID();
        LOGGER.error("Unexpected exception occurred {}: {}", uuid, ExceptionUtils.getStackTrace(e));
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        if ( null == rootCause ){
            rootCause = e;
        }
        String responseMessage = String.format("Unexpected exception of type %s occurred %s (ref %s)", rootCause.getClass().getName(), rootCause.getMessage(), uuid);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseMessage).build();
    }
}
