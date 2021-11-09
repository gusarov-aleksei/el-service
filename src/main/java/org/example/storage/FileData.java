package org.example.storage;

import java.util.Map;

/**
 * It holds document content and metadata
 */
public class FileData {

    public static final String FILE_NAME = "fileName";
    public static final String FILE_SIZE = "fileSize";
    public static final String TIME_CREATED = "timeCreated";

    public final Map<String, String> metadata;
    public final byte[] content; //TODO array is modifiable. to replace with immutable list?

    public FileData(Map<String, String> metadata, byte[] content) {
        this.metadata = metadata;
        this.content = content;
    }

    public static FileData of(Map<String, String> metadata, byte[] content) {
        return new FileData(metadata, content);
    }
}
