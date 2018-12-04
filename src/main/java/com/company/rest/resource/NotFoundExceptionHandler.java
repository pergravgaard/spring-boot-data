package com.company.rest.resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<PersonNotFound> {

    public Response toResponse(PersonNotFound ex) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
