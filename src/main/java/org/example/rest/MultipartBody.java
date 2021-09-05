package org.example.rest;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

/**
 * Representation of uploading file body
 */
public class MultipartBody {

    @NotNull
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    //public InputStream file;
    public byte[] file;

    @NotNull
    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    public String fileName;
}
