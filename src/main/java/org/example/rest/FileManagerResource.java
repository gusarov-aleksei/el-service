package org.example.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.example.file.FileOps;
import org.example.rest.error.ResponseGenerator;
import org.example.storage.StorageService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * This class contains http endpoints used for file uploading
 */
@OpenAPIDefinition(
        info = @Info(title = "File operations endpoint",
                description = "Endpoint exposed for file uploading, deleting",
                version = "Initial version")
)
@Path("/")
public class FileManagerResource implements ResponseGenerator, FileOps, FileDataExtractor {

    private final Logger LOGGER = LoggerFactory.getLogger(FileManagerResource.class);

    @Inject
    StorageService storageService;

    /**
     * Upload file implementation using in build data class MultipartFormDataInput which describes request data
     * @param request incoming data carrying filename and bytes of file content
     * @return file names of persisted files
     * @throws IOException error of working with file
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload file to the server (into source directory)",
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
            LOGGER.debug("File '{}' of size {} is going to be saved", nameToBody.getKey() ,nameToBody.getValue().length);
            storageService.writeBytesToFile(nameToBody.getKey(), nameToBody.getValue());
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
    @Operation(summary = "Upload file to the server (into source directory)")
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
        LOGGER.debug("File '{}' is going to be saved", data.fileName);
        storageService.writeBytesToFile(data.fileName, data.file);
        return Response.ok(data.fileName).build();
    }

    @Operation(summary = "Delete file at the server (located in source directory or at cloud storage)")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteFiles")
    public Response deleteFile(@NotNull @QueryParam("fileNames") String fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return badFileNameRequested();
        }
        return Response.ok(storageService.deleteFiles(fileNames.split(","))).build();
    }
}