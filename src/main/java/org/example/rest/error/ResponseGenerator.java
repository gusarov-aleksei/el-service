package org.example.rest.error;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.example.rest.error.Error.EMPTY_FILE_NAME;
import static org.example.rest.error.Error.FILE_NOT_FOUND;

public interface ResponseGenerator {

    default Response badFileNameRequested() {
        return genericResponse(Response.Status.BAD_REQUEST, EMPTY_FILE_NAME);
    }

    default Response fileNotFoundResponse() {
        return genericResponse(Response.Status.NOT_FOUND, FILE_NOT_FOUND);
    }

    default Response genericResponse(Response.Status status, Error error) {
        return Response.status(status).type(APPLICATION_JSON_TYPE)
                .entity(error.toEntity()).build();
    }
}
