package org.example.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * It provides with operations for working with files of local system
 */
public class FileOps {

    private String[] EMPTY_ARRAY = new String[0];

    public String[] getFiles(String absolutePath, String filePattern) throws IOException {
        var filesList = new File(absolutePath).list();
        if (filesList == null) {
            return EMPTY_ARRAY;
        }
        return Arrays.stream(filesList)
                .filter(name -> name.endsWith(filePattern))
                .toArray(String[]::new);
                //.collect(Collectors.toSet());
    }
}
