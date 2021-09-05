package org.example.rest.error;

/**
 * Defined errors enumeration
 */
public enum Error {
    INTERNAL_ERROR("ETL-0000", "Internal server error! Please ask administrator.."),
    EMPTY_FILE_NAME("ETL-0001", "Request parameter 'fileName' must be not empty"),
    FILE_NOT_FOUND("ETL-0002","The system cannot find the file specified"),
    EMPTY_FILE("ETL-0004", "Request parameter 'file' must be not empty");

    public final String code;
    public final String message;

    Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse toEntity() {
        return new ErrorResponse(code, message);
    }
}
