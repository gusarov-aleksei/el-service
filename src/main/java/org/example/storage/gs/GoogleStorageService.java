package org.example.storage.gs;

import org.example.storage.StorageService;
import org.jboss.resteasy.spi.NotImplementedYetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Instance of StorageService aimed for using Google Storage cloud service.
 */
public class GoogleStorageService implements StorageService {

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleStorageService.class);

    private final GsClient client;
    private final String defaultBucket;
    static final String NOT_FOUND_PATTERN = "File '%s' not found in bucket '%s'";

    public GoogleStorageService(GsClient client, String defaultBucket) {
        this.client = client;
        this.defaultBucket = defaultBucket;
    }

    @Override
    public String[] listFileNames(String dir, String filePattern) {
        //TODO to extend to pass filePattern (extension) GsClient
        //in this case of GS, relative dir isn't used
        LOGGER.debug("List object names from '{}'", defaultBucket);
        return client.listObjects(defaultBucket);
    }

    @Override
    public byte[] readBytesFormFile(String fileName) throws FileNotFoundException {
        LOGGER.debug("Read '{}' object from bucket '{}'", fileName, defaultBucket);
        return client.downloadObject(defaultBucket, fileName)
                .orElseThrow(
                        () -> new FileNotFoundException(String.format(NOT_FOUND_PATTERN, fileName, defaultBucket)));
    }

    @Override
    public void writeBytesToFile(String fileName, byte[] data) {
        LOGGER.debug("'{}' bytes will be persisted into '{}' object of bucket '{}' ", data.length, fileName, defaultBucket);
        client.uploadObject(defaultBucket, fileName, data);
    }

    @Override
    public List<Map.Entry<String, String>> deleteFiles(String[] fileNames) {
        //TODO to be implemented
        throw new NotImplementedYetException();
    }
}
