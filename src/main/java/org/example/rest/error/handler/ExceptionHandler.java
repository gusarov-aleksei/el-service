package org.example.rest.error.handler;

import org.example.rest.error.ResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static org.example.rest.error.Error.INTERNAL_ERROR;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception>, ResponseGenerator {

    private Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
    //TODO generalize approach
    @Override
    public Response toResponse(Exception e) {
        LOGGER.error("Error occurred!");
        e.printStackTrace();
        //don't return details to external requester
        return genericResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR);
    }
}
