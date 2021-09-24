package org.example.storage;

import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.properties.IfBuildProperty;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.example.application.QuarkusLifeCycle;
import org.example.service.SourceConfig;
import org.example.storage.gs.GoogleStorageService;
import org.example.storage.gs.GsClient;
import org.example.storage.local.LocalStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static io.quarkus.runtime.configuration.ProfileManager.getActiveProfile;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

/**
 * Here is example of Quarkus configuration https://quarkus.io/guides/cdi-reference
 * https://quarkus.io/guides/cdi
 * Depending on storage type class produces storage service implementation
 * (it looks like approach of Strategy design pattern)
 */
@Dependent
public class StorageServiceConfig {

    private Logger LOGGER = LoggerFactory.getLogger(StorageServiceConfig.class);

    @Inject
    SourceConfig config;

    @ConfigProperty(name = "storage.type")
    StorageType storageType;

    @Produces
    @IfBuildProperty(name = "storage.type", stringValue = "GS")
    public StorageService googleStorageService() {
        var bucket = getConfig().getOptionalValue("storage.gs.bucket", String.class).orElse("el-bucket");
        return new GoogleStorageService(createGsClient(), bucket);
    }

    private GsClient createGsClient() {
        if ("test".equals(getActiveProfile())) {
            LOGGER.info("FakeStorage will be used for 'test' profile");
            return GsClient.of(LocalStorageHelper.getOptions().getService());
        }
        var projectId = getConfig().getValue("storage.gs.project", String.class);
        LOGGER.info("GCP project '{}'",projectId);
        return GsClient.of(projectId);
    }

    @Produces
    @DefaultBean
    public StorageService localStorageService() {
        //TODO change bean configuration
        if (StorageType.GS.equals(getConfig().getValue("storage.type", StorageType.class))) {
            LOGGER.info("GoogleStorageService implementation is used");
            var bucket = getConfig().getOptionalValue("storage.gs.bucket", String.class).orElse("el-bucket");
            return new GoogleStorageService(createGsClient(), bucket);
        }
        LOGGER.info("LocalStorageService implementation is used");
        return new LocalStorageService(config.directory());
    }
}
