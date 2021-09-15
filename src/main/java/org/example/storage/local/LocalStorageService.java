package org.example.storage.local;

import org.example.file.FileOps;
import org.example.storage.StorageService;

import java.io.IOException;
import java.util.Optional;

/**
 * Instance of Storage Service operated with local file system
 */
public class LocalStorageService implements StorageService, FileOps {
    //it ends with slash '/'
    private final String sourceDir;

    public LocalStorageService(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Override
    public String[] listFileNames(String dir, String filePattern) throws IOException {
        return getFiles(sourceDir + dir, filePattern);
    }

    @Override
    public Optional<byte[]> readBytesFormFile(String fileName) {
        //TODO to be implemented
        return Optional.empty();
    }

    @Override
    public void writeBytesToFile(String fileName, byte[] data) throws IOException {
        this.saveIntoFile(sourceDir + fileName, data);
    }
}
