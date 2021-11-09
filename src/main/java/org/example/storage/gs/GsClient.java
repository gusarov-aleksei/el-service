package org.example.storage.gs;

import com.google.api.gax.paging.Page;
import com.google.cloud.BatchResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageBatch;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.cloud.storage.Storage.BlobField.TIME_CREATED;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.StreamSupport.stream;

/**
 * Facade over Google Storage API
 */
public class GsClient {

    private final Storage storage;
    // this flag is required for workaround for locally running Storage
    // because not all features are implemented in library for local testing 'google-cloud-nio'
    private final boolean fakeStorage;

    private GsClient(Storage storage, boolean fakeStorage) {
        this.storage = storage;
        this.fakeStorage = fakeStorage;
    }
    // build client with fake storage for local testing
    public static GsClient buildWithFakeStorage() {
        return new GsClient(LocalStorageHelper.getOptions().getService(), true);
    }

    public static GsClient of(String projectId) {
        return new GsClient(StorageOptions.newBuilder().setProjectId(projectId).build().getService(), false);
    }

    public String uploadObject(String bucketName, String name, byte[] data) {
        var blobId = BlobId.of(bucketName, name);
        var blobMetadata = Map.of(TIME_CREATED.getSelector(), String.valueOf(System.currentTimeMillis()));
        var blobInfo = BlobInfo.newBuilder(blobId).setMetadata(blobMetadata).build();
        return storage.create(blobInfo, data).getName();
    }

    public String[] listObjects(String bucketName) {
        Page<Blob> blobs = storage.list(bucketName);
        return stream(blobs.iterateAll().spliterator(), false)
                .map(Blob::getName).toArray(String[]::new);
    }

    public Optional<Blob> downloadObject(String bucketName, String objectName) {
        return Optional.ofNullable(storage.get(BlobId.of(bucketName, objectName)));
    }

    private List<Map.Entry<String, String>> deleteObjectsInSingleMode(String bucketName, String[] objectNames) {
        // this deletion is not in bulk mode
        var result = new ArrayList<Map.Entry<String, String>>();
        for (String name:  objectNames) {
            var deleted = storage.delete(BlobId.of(bucketName, name));
            result.add(Map.entry(name, deleted ? "OK" : "Not OK"));
        }
        return result;
    }

    public List<Map.Entry<String, String>> deleteObjects(String bucketName, String[] objectNames) {
        if (objectNames.length == 0) {
            return emptyList();
        }
        // workaround for locally running Storage.
        // object deletion in bulk mode (in one remote call) is not implemented in current version of library for testing 'google-cloud-nio'
        if (fakeStorage) {
            return deleteObjectsInSingleMode(bucketName, objectNames);
        }
        return deleteObjectsInBatchMode(bucketName, objectNames);

    }

    private List<Map.Entry<String, String>> deleteObjectsInBatchMode(String bucketName, String[] objectNames) {
        StorageBatch batch = storage.batch();
        final var results = new ArrayList<Map.Entry<String, String>>();
        for (String name : objectNames) {
            batch.delete(BlobId.of(bucketName, name)).notify(
                    new BatchResult.Callback<>() {
                        @Override
                        public void success(Boolean result) {
                            results.add(new SimpleImmutableEntry<>(name, "OK"));
                        }
                        @Override
                        public void error(StorageException exception) {
                            results.add(new SimpleImmutableEntry<>(name, exception.getMessage()));
                            exception.printStackTrace();
                        }
                    });
        }
        batch.submit();
        return unmodifiableList(results);
    }

}
