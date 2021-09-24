package org.example.storage.gs;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.util.Optional;

import static java.util.stream.StreamSupport.stream;

/**
 * Facade over Google Storage API
 */
public class GsClient {

    private final Storage storage;

    public GsClient(Storage storage) {
        this.storage = storage;
    }

    public static GsClient of(Storage storage) {
        return new GsClient(storage);
    }

    public static GsClient of(String projectId) {
        return new GsClient(StorageOptions.newBuilder().setProjectId(projectId).build().getService());
    }

    public String uploadObject(String bucketName, String name, byte[] data) {
        BlobId blobId = BlobId.of(bucketName, name);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        return storage.create(blobInfo, data).getName();
    }

    public String[] listObjects(String bucketName) {
        Page<Blob> blobs = storage.list(bucketName);
        return stream(blobs.iterateAll().spliterator(), false)
                .map(Blob::getName).toArray(String[]::new);
    }

    public Optional<byte[]> downloadObject(String bucketName, String objectName) {
        return Optional.ofNullable(storage.get(BlobId.of(bucketName, objectName)))
                .map(Blob::getContent);
    }

}
