package org.example.application;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

@ApplicationScoped
public class QuarkusLifeCycle {

    private Logger LOGGER = LoggerFactory.getLogger(QuarkusLifeCycle.class);

    void onStart(@Observes StartupEvent ev) {
        Config config = getConfig();
        var sourceDir = config.getValue("storage.source.directory", String.class);
        if (sourceDir == null || sourceDir.isBlank()) {
            LOGGER.error("System variable 'LOCAL_SOURCE_DIR' is not set. Check parameter 'LOCAL_SOURCE_DIR'/'storage.source.directory'");
        } else {
            LOGGER.info("Source directory is '{}'", sourceDir);
        }
    }
}
