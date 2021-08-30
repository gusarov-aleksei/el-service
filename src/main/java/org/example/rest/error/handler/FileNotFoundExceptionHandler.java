package org.example.rest.error.handler;

import org.example.rest.error.ResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.FileNotFoundException;

@Provider
public class FileNotFoundExceptionHandler implements ExceptionMapper<FileNotFoundException>, ResponseGenerator {

    private Logger LOGGER = LoggerFactory.getLogger(FileNotFoundException.class);

    @Override
    public Response toResponse(FileNotFoundException e) {
        LOGGER.error("FileNotFoundException occurred!");
        e.printStackTrace();
        return fileNotFoundResponse() ;
    }
}
