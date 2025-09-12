package com.simsekolah.exception;

/**
 * Exception thrown when attempting to create a user that already exists
 */
public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String identifier) {
        super("User already exists: " + identifier);
    }

    public UserAlreadyExistsException(String field, String value) {
        super("User already exists with " + field + ": " + value);
    }
}