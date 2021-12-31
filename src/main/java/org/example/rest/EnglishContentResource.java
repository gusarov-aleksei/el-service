package org.example.rest;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.example.rest.error.ResponseGenerator;
import org.example.service.EnglishContentService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


@OpenAPIDefinition(
        info = @Info(title = "english-content-service",
                description = "English content extractor service",
                version = "Initial version")
)
@Path("/")
public class EnglishContentResource implements ResponseGenerator {

    @Inject
    EnglishContentService pdfService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public String[] listFiles() {
        return pdfService.listDocumentNames();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("extract")
    public Response eslContent(@NotNull @QueryParam("fileName") String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            return badFileNameRequested();
        }
        return Response.ok(pdfService.extractContent(fileName)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persist")
    public Response persistContent(@NotNull @QueryParam("fileName") String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            return badFileNameRequested();
        }
        return Response.ok(pdfService.persistContent(fileName)).build();
    }
}