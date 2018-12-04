package com.company.rest.resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AlreadyExistsExceptionHandler  implements ExceptionMapper<PersonAlreadyExists> {

    public Response toResponse(PersonAlreadyExists ex) {
        return Response.status(Response.Status.CONFLICT.getStatusCode()).build();
    }

}
