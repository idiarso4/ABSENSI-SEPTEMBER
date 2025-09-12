package com.simsekolah.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String fieldName, String message) {
        super(String.format("Validation error for field '%s': %s", fieldName, message));
    }
}
