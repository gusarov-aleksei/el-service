package org.example.storage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.example.storage.gs.GoogleStorageService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(GoogleStorageServiceTestProfile.class)
public class GoogleStorageServiceTest {

    @Inject
    StorageService storageService;

    /**
     * Tests instance class returned according to configuration
     */
    @Test
    public void testStorageServiceConfig_whenStorageTypeIsGs_shouldBeGoogleStorageService() {
        assertThat(storageService).isNotNull();
        assertThat(storageService).isExactlyInstanceOf(GoogleStorageService.class);
    }
}
