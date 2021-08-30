package org.example.pdf;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface ResourceLoader {
    default URI getUri(String relativePath) throws URISyntaxException {
        return  getClass().getClassLoader().getResource(relativePath).toURI();
    }

    default String readStringFromFile(String relativePath) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(getUri(relativePath)));
    }

    default File getFile(String relativePath) throws URISyntaxException {
        return new File(getUri(relativePath));
    }

    default String absolutPathToDirectory(String relativePath) {
        return getClass().getClassLoader().getResource(relativePath).getFile();
    }
}
