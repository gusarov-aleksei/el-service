package org.example.application;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.Config;
import org.example.storage.StorageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Optional;

import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

@ApplicationScoped
public class QuarkusLifeCycle {

    private Logger LOGGER = LoggerFactory.getLogger(QuarkusLifeCycle.class);

    void onStart(@Observes StartupEvent ev) {
        Config config = getConfig();
        validateGoogleStorageConfig(config);
        validateLocalStorageConfig(config);
    }

    private void validateLocalStorageConfig(Config config) {
        var sourceDir = config.getValue("storage.source.directory", String.class);
        if (sourceDir == null || sourceDir.isBlank()) {
            LOGGER.error("System variable 'LOCAL_SOURCE_DIR' is not set. Check parameter 'LOCAL_SOURCE_DIR'/'storage.source.directory'");
        } else {
            LOGGER.info("Source directory is '{}'", sourceDir);
        }
    }

    private void validateGoogleStorageConfig(Config config) {
        var storageType = config.getValue("storage.type", StorageType.class);
        LOGGER.info("Storage type set '{}'", storageType);
        if (StorageType.GS.equals(storageType)) {
            validateConfigProperty(config, "storage.gs.project");
            validateConfigProperty(config, "storage.gs.bucket");
        }
    }

    private void validateConfigProperty(Config config, String propertyName) {
        Optional<String> project = config.getOptionalValue(propertyName, String.class);
        if (project.isEmpty()) {
            LOGGER.error("Config property '{}' is not set!!! Check config", propertyName);
        }
    }
}
