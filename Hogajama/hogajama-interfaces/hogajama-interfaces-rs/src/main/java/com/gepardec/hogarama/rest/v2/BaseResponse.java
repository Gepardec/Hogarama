package com.gepardec.hogarama.rest.v2;

import javax.ws.rs.core.Response;

public class BaseResponse<T> {
    private T response;
    private int statusCode;

    public BaseResponse(T response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public BaseResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response createRestResponse() {
        return Response.status(statusCode).entity(this).build();
    }
}
