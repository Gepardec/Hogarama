package com.gepardec.hogarama.rest;

import com.google.common.net.HttpHeaders;
import org.jboss.resteasy.spi.DefaultOptionsMethodException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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
