package org.example.storage;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.Map;

/**
 * Profile is being used for StorageService instance class validation.
 * StorageService instance class depends on Storage type set in config:
 * If type is LOCAL then instance would be LocalStorageService
 */
public class LocalStorageServiceTestProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap("%test.storage.type", "LOCAL");
    }

    @Override
    public String getConfigProfile() {
        return "test";
    }
}
