package com.gepardec.hogarama.rest;

import com.google.common.net.HttpHeaders;
import org.jboss.resteasy.spi.DefaultOptionsMethodException;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for returning a standardized response for all OPTIONS requests
 */
@Provider
public class DefaultOptionsMethodExceptionMapper implements ExceptionMapper<DefaultOptionsMethodException> {

    @Override
    public Response toResponse(DefaultOptionsMethodException exception) {
        return Response.ok()
                .header(HttpHeaders.ALLOW, HttpMethod.GET)
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE)
                .header(HttpHeaders.ALLOW, HttpMethod.OPTIONS)
                .header(HttpHeaders.ALLOW, HttpMethod.HEAD)
                .build();
    }
}
