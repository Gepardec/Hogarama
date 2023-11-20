package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.rest.unitmanagement.BaseResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Map occurring exceptions to a standard response
 */
@Provider
public class HogaramaExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(HogaramaExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        String message = exception.getMessage();
        LOG.error("Exception occured: {}", message, exception);
        BaseResponse<Object> response = new BaseResponse<>(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        response.setMessage(message);

        return response.createRestResponse();
    }

}

