package com.simsekolah.exception;

/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }

    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }
}
