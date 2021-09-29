package org.example.storage.local;

import org.example.file.FileOps;
import org.example.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Instance of Storage Service operated with local file system
 */
public class LocalStorageService implements StorageService, FileOps {

    private final Logger LOGGER = LoggerFactory.getLogger(LocalStorageService.class);
    //it ends with slash '/'
    private final String sourceDir;

    public LocalStorageService(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Override
    public String[] listFileNames(String relativeDir, String filePattern) {
        var absolutePath = sourceDir + relativeDir;
        LOGGER.info("Request for files list '{}' in '{}'", filePattern, absolutePath);
        return getFiles(absolutePath, filePattern);
    }

    @Override
    public byte[] readBytesFormFile(String fileName) throws IOException {
        var absolutePath = sourceDir + fileName;
        LOGGER.info("Request for file reading '{}'", absolutePath);
        return readFromFile(absolutePath);
    }

    @Override
    public void writeBytesToFile(String fileName, byte[] data) throws IOException {
        var absolutePath = sourceDir + fileName;
        LOGGER.info("Request for file writing '{}' with data length {}", absolutePath, data.length);
        this.saveIntoFile(absolutePath, data);
    }

    @Override
    public List<Map.Entry<String, String>> deleteFiles(String[] fileNames) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request to delete files '{}'", Arrays.toString(fileNames));
        }
        return deleteFiles(fileNames, sourceDir);
    }
}
