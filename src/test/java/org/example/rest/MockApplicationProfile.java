package org.example.rest;

import io.quarkus.test.junit.QuarkusTestProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

import static io.quarkus.runtime.configuration.ProfileManager.getActiveProfile;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

//TODO to reduce complexity, just always set LOCAL_SOURCE_DIR environment variable
// at present moment this Profile is not required for source.directory substitution
/**
 * Quarkus test profile used in unit-test with QuarkusTest annotation.
 */
public class MockApplicationProfile implements QuarkusTestProfile {

    private final Logger LOGGER = LoggerFactory.getLogger(MockApplicationProfile.class);

    @Override
    public Map<String, String> getConfigOverrides() {
        LOGGER.info("Active profile is '{}'", getActiveProfile());
        LOGGER.info("source dir is '{}'", getConfig().getValue("storage.source.directory", String.class));
        //setup directory where files will be placed while QuarkusTest's be running
        var relativeDir = getConfig().getValue("testing.relative-dir", String.class);
        LOGGER.info("testing.relative-dir is {}", relativeDir);
        var absolutePath = getClass().getClassLoader().getResource(relativeDir).getPath();
        LOGGER.info("storage.source.directory will be set to {}", absolutePath);
        //expected 'absolutePath' would be something like 'target/test-classes/pdf' - all compiled test classes are put into 'target/test-classes'
        //return Collections.singletonMap("%test.storage.source.directory", absolutePath);
        return Collections.emptyMap();
    }

    @Override
    public String getConfigProfile() {
        return "test";
    }
}
