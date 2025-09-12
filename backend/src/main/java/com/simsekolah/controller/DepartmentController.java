package com.simsekolah.controller;

import com.simsekolah.entity.Department;
import com.simsekolah.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for Department operations
 * Provides endpoints for managing academic departments
 */
@RestController
@RequestMapping({"/api/v1/departments", "/api/departments"})
@Tag(name = "Departments", description = "Department management endpoints")
@Validated
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * Get all active departments
     */
    @GetMapping
    @Operation(summary = "Get all departments", description = "Retrieve all active departments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    })
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllActiveDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * Get departments with pagination
     */
    @GetMapping("/paged")
    @Operation(summary = "Get departments with pagination", description = "Retrieve departments with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    })
    public ResponseEntity<Page<Department>> getDepartmentsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.getAllActiveDepartments(pageable);
        return ResponseEntity.ok(departments);
    }

    /**
     * Get department by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieve a specific department by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Department found"),
        @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            return ResponseEntity.ok(department.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get department by code
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Get department by code", description = "Retrieve a department by its code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Department found"),
        @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<Department> getDepartmentByCode(@PathVariable String code) {
        Optional<Department> department = departmentService.getDepartmentByCode(code);
        if (department.isPresent()) {
            return ResponseEntity.ok(department.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new department
     */
    @PostMapping
    @Operation(summary = "Create department", description = "Create a new department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Department created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Department code or name already exists")
    })
    public ResponseEntity<?> createDepartment(@Valid @RequestBody Department department) {
        try {
            Department createdDepartment = departmentService.createDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * Update department
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update department", description = "Update an existing department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Department updated successfully"),
        @ApiResponse(responseCode = "404", description = "Department not found"),
        @ApiResponse(responseCode = "409", description = "Department code or name already exists")
    })
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department department) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(id, department);
            return ResponseEntity.ok(updatedDepartment);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
        }
    }

    /**
     * Delete department
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department", description = "Delete a department (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search departments by name
     */
    @GetMapping("/search")
    @Operation(summary = "Search departments", description = "Search departments by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<Department>> searchDepartments(@RequestParam String name) {
        List<Department> departments = departmentService.searchDepartmentsByName(name);
        return ResponseEntity.ok(departments);
    }

    /**
     * Get department statistics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get department statistics", description = "Get statistics about departments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getDepartmentStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalActiveDepartments", departmentService.countActiveDepartments());
        stats.put("departmentsWithMajors", departmentService.getDepartmentsWithMajors().size());
        stats.put("departmentsWithoutMajors", departmentService.getDepartmentsWithoutMajors().size());

        return ResponseEntity.ok(stats);
    }

    /**
     * Check if department code exists
     */
    @GetMapping("/exists/code/{code}")
    @Operation(summary = "Check code existence", description = "Check if a department code already exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Existence check completed")
    })
    public ResponseEntity<Map<String, Boolean>> checkCodeExists(@PathVariable String code) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", departmentService.existsByCode(code));
        return ResponseEntity.ok(result);
    }

    /**
     * Check if department name exists
     */
    @GetMapping("/exists/name/{name}")
    @Operation(summary = "Check name existence", description = "Check if a department name already exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Existence check completed")
    })
    public ResponseEntity<Map<String, Boolean>> checkNameExists(@PathVariable String name) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", departmentService.existsByName(name));
        return ResponseEntity.ok(result);
    }
}