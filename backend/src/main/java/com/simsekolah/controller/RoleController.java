package com.simsekolah.controller;

import com.simsekolah.entity.Permission;
import com.simsekolah.entity.Role;
import com.simsekolah.service.RoleService;
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
import java.util.Set;

/**
 * REST controller for Role operations
 * Provides endpoints for managing user roles and permissions
 */
@RestController
@RequestMapping({"/api/v1/roles", "/api/roles"})
@Tag(name = "Roles", description = "Role management endpoints")
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * Get all roles
     */
    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieve all roles in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    })
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Get roles with pagination
     */
    @GetMapping("/paged")
    @Operation(summary = "Get roles with pagination", description = "Retrieve roles with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    })
    public ResponseEntity<Page<Role>> getRolesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleService.getAllRoles(pageable);
        return ResponseEntity.ok(roles);
    }

    /**
     * Get role by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieve a specific role by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role found"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get role by name
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Get role by name", description = "Retrieve a role by its name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role found"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Optional<Role> role = roleService.getRoleByName(name);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get system roles
     */
    @GetMapping("/system")
    @Operation(summary = "Get system roles", description = "Retrieve all system roles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "System roles retrieved successfully")
    })
    public ResponseEntity<List<Role>> getSystemRoles() {
        List<Role> roles = roleService.getSystemRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Get non-system roles
     */
    @GetMapping("/custom")
    @Operation(summary = "Get custom roles", description = "Retrieve all non-system roles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Custom roles retrieved successfully")
    })
    public ResponseEntity<List<Role>> getCustomRoles() {
        List<Role> roles = roleService.getNonSystemRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Create new role
     */
    @PostMapping
    @Operation(summary = "Create role", description = "Create a new role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Role created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Role name already exists")
    })
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * Update role
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update role", description = "Update an existing role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role updated successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "409", description = "Role name already exists")
    })
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
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
     * Delete role
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role", description = "Delete a role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete system role")
    })
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
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
     * Search roles by name
     */
    @GetMapping("/search")
    @Operation(summary = "Search roles", description = "Search roles by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<Role>> searchRoles(@RequestParam String name) {
        List<Role> roles = roleService.searchRolesByName(name);
        return ResponseEntity.ok(roles);
    }

    /**
     * Get roles with users
     */
    @GetMapping("/with-users")
    @Operation(summary = "Get roles with users", description = "Get roles that are assigned to users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    })
    public ResponseEntity<List<Role>> getRolesWithUsers() {
        List<Role> roles = roleService.getRolesWithUsers();
        return ResponseEntity.ok(roles);
    }

    /**
     * Get roles without users
     */
    @GetMapping("/without-users")
    @Operation(summary = "Get roles without users", description = "Get roles that are not assigned to any users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    })
    public ResponseEntity<List<Role>> getRolesWithoutUsers() {
        List<Role> roles = roleService.getRolesWithoutUsers();
        return ResponseEntity.ok(roles);
    }

    /**
     * Get roles by user ID
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user roles", description = "Get roles assigned to a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User roles retrieved successfully")
    })
    public ResponseEntity<List<Role>> getRolesByUserId(@PathVariable Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        return ResponseEntity.ok(roles);
    }

    /**
     * Get permissions for role
     */
    @GetMapping("/{id}/permissions")
    @Operation(summary = "Get role permissions", description = "Get permissions assigned to a role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<?> getRolePermissions(@PathVariable Long id) {
        try {
            Set<Permission> permissions = roleService.getPermissionsForRole(id);
            return ResponseEntity.ok(permissions);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get role statistics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get role statistics", description = "Get statistics about roles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getRoleStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRoles", roleService.getAllRoles().size());
        stats.put("systemRoles", roleService.countBySystemRole(true));
        stats.put("customRoles", roleService.countBySystemRole(false));
        stats.put("rolesWithUsers", roleService.getRolesWithUsers().size());
        stats.put("rolesWithoutUsers", roleService.getRolesWithoutUsers().size());

        return ResponseEntity.ok(stats);
    }

    /**
     * Check if role name exists
     */
    @GetMapping("/exists/name/{name}")
    @Operation(summary = "Check name existence", description = "Check if a role name already exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Existence check completed")
    })
    public ResponseEntity<Map<String, Boolean>> checkNameExists(@PathVariable String name) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", roleService.existsByName(name));
        return ResponseEntity.ok(result);
    }
}