package org.example.rest.error;

/**
 * It uses for runtime error responding
 */
public class ErrorResponse {
    public final String code;
    public final String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}