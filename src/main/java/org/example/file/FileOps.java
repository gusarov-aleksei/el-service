package org.example.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

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

    /**
     * Delete files by fileNames from absolutePathDirectory
     * @param fileNames array of file names to delete
     * @param absolutePathDirectory absolute path where files are located
     * @return array of key-value with meaning fileName -> operation result
     */
    default List<Map.Entry<String, String>> deleteFiles(String[] fileNames, String absolutePathDirectory) {
        if (fileNames.length == 0) {
            return emptyList();
        }
        var result = new ArrayList<Map.Entry<String, String>>();
        for (String fileName : fileNames) {
            try {
                Files.delete(Paths.get(absolutePathDirectory + fileName));
                result.add(Map.entry(fileName, "OK"));
            } catch (IOException e) {
                result.add(Map.entry(fileName, e.getClass().getName() + ": " + e.getMessage()));
                e.printStackTrace();
            }
        }
        return unmodifiableList(result);
    }
}
