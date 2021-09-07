package org.example.rest;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static javax.ws.rs.core.HttpHeaders.CONTENT_DISPOSITION;

/**
 * Logic for file name and file data retrieving from MultipartFormDataInput request.
 * It is used by file uploading logic to extract file body and save it into file with extracted name.
 */
public interface FileDataExtractor {

    Logger LOGGER = LoggerFactory.getLogger(FileDataExtractor.class);

    //Content-Disposition literals
    String FILENAME = "filename";
    String FILE = "file";

    /**
     * Retrieves name of file from Content-Disposition header of body prat
     * (body is split into parts by 'boundary' mark, see example in src/test/java/org/example/rest/upload_call.log )
     * @param inputPart part of incoming request to retrieve file name from it
     * @return filename if it was found in inputPart
     */
    default Optional<String> retrieveFileName(InputPart inputPart) {
        var contentDispositions = inputPart.getHeaders().getFirst(CONTENT_DISPOSITION);
        if (contentDispositions == null) {
            return Optional.empty();
        }
        for (String contentDisposition : contentDispositions.split(";")) {
            if (contentDisposition.trim().startsWith(FILENAME)) {
                var delimiter = contentDisposition.indexOf('=');
                if (delimiter > -1) {
                    return Optional.of(contentDisposition.substring(delimiter + 1).replaceAll("\"", ""));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieve body of file from inputPart (part of MultipartFormDataInput) as array of bytes
     * @param inputPart part of MultipartFormDataInput
     * @return content of file
     * @throws IOException internal error of working with file
     */
    default byte[] retrieveFileBody(InputPart inputPart) throws IOException {
        var inputStream = inputPart.getBody(InputStream.class, null);
        return inputStream.readAllBytes();
    }

    /**
     * Retrieve files name and body from multipart input request
     * @param multiplePartList - multi InputPart gotten from MultipartFormDataInput
     * @return file name to file body (as bytes) map
     */
    default Map<String, byte[]> retrieveFiles(List<InputPart> multiplePartList) throws IOException {
        if (multiplePartList.isEmpty()) {
            LOGGER.info("No multiple parts in input request");
            return emptyMap();
        }
        var fileNameToBody = new HashMap<String, byte[]>();
        for (var inputPart : multiplePartList) {
            var fileName = retrieveFileName(inputPart);
            if (fileName.isPresent()) {
                fileNameToBody.put(fileName.get(), retrieveFileBody(inputPart));
            } else {
                LOGGER.error("data name not found in request input. input data is not saved");
            }
        }
        return fileNameToBody;
    }

}
