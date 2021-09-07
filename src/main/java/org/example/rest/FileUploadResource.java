package org.example.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.example.file.FileOps;
import org.example.rest.error.ResponseGenerator;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * This class contains http endpoints used for file uploading
 */
@OpenAPIDefinition(
        info = @Info(title = "english-content-service",
                description = "English pdf files uploader",
                version = "Initial version")
)
@Path("/")
public class FileUploadResource implements ResponseGenerator, FileOps, FileDataExtractor {

    private Logger LOGGER = LoggerFactory.getLogger(FileUploadResource.class);

    @ConfigProperty(name = "storage.source.directory")
    String sourceDir;

    /**
     * Upload file implementation using in build data class MultipartFormDataInput which describes request data
     * @param request incoming data carrying filename and bytes of file content
     * @return file names of persisted files
     * @throws IOException error of working with file
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload file the server (into source directory)",
            description = "This API is implemented with possibility of several files uploading at one request"
    )
    @Path("uploadMulti")
    public Response uploadFiles(@MultipartForm MultipartFormDataInput request) throws IOException {
        //transform input into file content
        if (request.getFormDataMap().get(FILE) == null) {
            LOGGER.debug("file doesn't present in input");
            return Response.serverError().entity("file doesn't present in input").build();
        }
        //decouple and make map of <file_name, byte[]>
        var fileToBodyMap = retrieveFiles(request.getFormDataMap().get(FILE));
        for (var nameToBody : fileToBodyMap.entrySet()) {
            LOGGER.debug("File '{}' is going to be saved into {}", nameToBody.getKey(), sourceDir);
            saveIntoFile(sourceDir + "/" + nameToBody.getKey(), nameToBody.getValue());
        }
        //return persisted file names
        return Response.ok(fileToBodyMap.keySet()).build();
    }

    /**
     * Upload file implementation with simpler and lightweight data structure used for info about file.
     * It works for single file uploading.
     * @param data info about uploaded file
     * @return name of persisted file
     * @throws IOException error of working with IO File API
     */
    @Operation(summary = "Upload file the server (into source directory)")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("upload")
    public Response sendMultipartData(@MultipartForm MultipartBody data)  throws IOException {
        if (data.fileName == null || data.fileName.isBlank()) {
            return badFileNameRequested();
        }
        if (data.file == null || data.file.length <= 2) {
            return badFileRequested();
        }
        LOGGER.debug("File '{}' is going to be saved into {}", data.fileName, sourceDir);
        saveIntoFile(sourceDir + data.fileName, data.file);
        return Response.ok(data.fileName).build();
    }
}