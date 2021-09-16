package org.example.storage.gs;

import org.example.storage.StorageService;

import java.io.FileNotFoundException;

/**
 * Instance of StorageService aimed for using Google Storage cloud service.
 */
public class GoogleStorageService implements StorageService {

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
        //in this case of GS, relative dir is bucket name
        return client.listObjects(dir);
    }

    @Override
    public byte[] readBytesFormFile(String fileName) throws FileNotFoundException {
        return client.downloadObject(defaultBucket, fileName)
                .orElseThrow(
                        () -> new FileNotFoundException(String.format(NOT_FOUND_PATTERN, fileName, defaultBucket)));
    }

    @Override
    public void writeBytesToFile(String fileName, byte[] data) {
       client.uploadObject(defaultBucket, fileName, data);
    }
}
