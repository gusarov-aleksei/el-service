package org.example.storage;

import java.util.Collections;
import java.util.Map;

/**
 * Profile is being used for StorageService instance class validation.
 * StorageService instance class depends on Storage type set in config.
 * If type is Google storage then GoogleStorageService instance is expected
 */
public class GoogleStorageServiceTestProfile extends LocalStorageServiceTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap("%test.storage.type", "GS");
    }
}
