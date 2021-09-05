package org.example.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * It provides with operations for working with files of local system
 */
public interface FileOps {

    String[] EMPTY_ARRAY = new String[0];

    default String[] getFiles(String absolutePath, String filePattern) throws IOException {
        var filesList = new File(absolutePath).list();
        if (filesList == null) {
            return EMPTY_ARRAY;
        }
        return Arrays.stream(filesList)
                .filter(name -> name.endsWith(filePattern))
                .toArray(String[]::new);
                //.collect(Collectors.toSet());
    }

     default void saveIntoFile(byte[] bytesToSave, String absolutePath) throws IOException {
        try (var outputStream = new FileOutputStream(absolutePath)){
            //write all bytes directly without buffering for simplicity
            outputStream.write(bytesToSave);
        }
    }
}
