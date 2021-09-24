package org.example.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * It provides with operations for working with files of local system
 */
public interface FileOps {

    String[] EMPTY_ARRAY = new String[0];

    /**
     * Retrieve file names placed in directory 'absolutePath'
     * @param absolutePath absolute path directory
     * @param filePattern file extension like '*.pdf'
     * @return array of file names
     */
    default String[] getFiles(String absolutePath, String filePattern) {
        var filesList = new File(absolutePath).list();
        if (filesList == null) {
            return EMPTY_ARRAY;
        }
        return Arrays.stream(filesList)
                .filter(name -> name.endsWith(filePattern))
                .toArray(String[]::new);
                //.collect(Collectors.toSet());
    }

    /**
     * Save array of bytes into file with name 'absolutePath' (path + name)
     * @param absolutePath absolute path to file (path + name)
     * @param bytesToSave array of bytes
     * @throws IOException internal error of working with file
     */
     default void saveIntoFile(String absolutePath, byte[] bytesToSave) throws IOException {
        try (var outputStream = new FileOutputStream(absolutePath)){
            //write all bytes directly without buffering for simplicity
            outputStream.write(bytesToSave);
        }
    }

    /**
     * Get file content as array of bytes
     * @param fileName absolute file path + name
     * @return content of file
     * @throws IOException in case of internal error
     */
    default byte[] readFromFile(String fileName) throws IOException {
        try (var inputStream = new FileInputStream(fileName)) {
            return inputStream.readAllBytes();
        }
    }
}
