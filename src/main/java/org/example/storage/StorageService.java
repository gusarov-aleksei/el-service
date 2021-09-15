package org.example.storage;

import java.io.IOException;
import java.util.Optional;

/**
 * Base API for working with Storage (place where blob data is located)
 */
public interface StorageService {
    /**
     * Retrieve array of file names placed at directory
     * @param relativeDir relative directory
     * @param filePattern part of file name for filtering
     * @return array of names of file
     * @throws IOException internal IO error
     */
    String[] listFileNames(String relativeDir, String filePattern) throws IOException;

    /**
     * Retrieves file content
     * @param fileName name of the file (relative path + file name)
     * @return array of bytes
     */
    Optional<byte[]> readBytesFormFile(String fileName);

    /**
     * Save array of bytes into file with fileName
     * @param fileName name of the file (relative path + file name)
     * @param data content of the file
     * @throws IOException internal IO error
     */
    void writeBytesToFile(String fileName, byte[] data) throws IOException;

}
