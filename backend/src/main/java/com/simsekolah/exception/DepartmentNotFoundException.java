package com.simsekolah.exception;

/**
 * Exception thrown when a department is not found
 */
public class DepartmentNotFoundException extends BusinessException {

    public DepartmentNotFoundException(String identifier) {
        super("Department not found: " + identifier);
    }

    public DepartmentNotFoundException(Long departmentId) {
        super("Department not found with ID: " + departmentId);
    }
}