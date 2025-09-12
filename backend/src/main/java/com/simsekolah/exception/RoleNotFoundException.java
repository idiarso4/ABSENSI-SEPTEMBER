package com.simsekolah.exception;

/**
 * Exception thrown when a role is not found
 */
public class RoleNotFoundException extends BusinessException {

    public RoleNotFoundException(String identifier) {
        super("Role not found: " + identifier);
    }

    public RoleNotFoundException(Long roleId) {
        super("Role not found with ID: " + roleId);
    }
}