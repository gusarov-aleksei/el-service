package org.example.storage.gs;

import org.example.storage.StorageService;

import java.util.Optional;

/**
 * Instance of StorageService aimed for using Google Storage cloud service.
 */
public class GoogleStorageService implements StorageService {

    private final GsClient client;
    private final String defaultBucket;

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
    public Optional<byte[]> readBytesFormFile(String fileName) {
        return client.downloadObject(defaultBucket, fileName);
    }

    @Override
    public void writeBytesToFile(String fileName, byte[] data) {
       client.uploadObject(defaultBucket, fileName, data);
    }
}
