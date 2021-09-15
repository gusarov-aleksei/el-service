package org.example.storage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.example.storage.local.LocalStorageService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(LocalStorageServiceTestProfile.class)
public class LocalStorageServiceTest {

    @Inject
    StorageService storageService;

    /**
     * Tests instance class returned according to configuration
     */
    @Test
    public void testStorageServiceConfig_whenStorageTypeLocal_shouldBeLocalStorageService() {
        assertThat(storageService).isNotNull();
        assertThat(storageService).isExactlyInstanceOf(LocalStorageService.class);
    }
}
