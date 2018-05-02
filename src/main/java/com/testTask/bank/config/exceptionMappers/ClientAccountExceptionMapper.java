package com.testTask.bank.config.exceptionMappers;

import com.testTask.bank.exception.ClientAccountException;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Provider
public class ClientAccountExceptionMapper implements ExceptionMapper<ClientAccountException> {

    @Override
    public Response toResponse(ClientAccountException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
