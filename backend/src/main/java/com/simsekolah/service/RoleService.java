package com.simsekolah.service;

import com.simsekolah.entity.Permission;
import com.simsekolah.entity.Role;
import com.simsekolah.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for Role operations
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Get all roles with pagination
     */
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    /**
     * Get role by ID
     */
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Get role by name
     */
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Get system roles
     */
    public List<Role> getSystemRoles() {
        return roleRepository.findByIsSystemRoleTrue();
    }

    /**
     * Get non-system roles
     */
    public List<Role> getNonSystemRoles() {
        return roleRepository.findByIsSystemRoleFalse();
    }

    /**
     * Get non-system roles with pagination
     */
    public Page<Role> getNonSystemRoles(Pageable pageable) {
        return roleRepository.findByIsSystemRoleFalse(pageable);
    }

    /**
     * Create new role
     */
    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role name already exists: " + role.getName());
        }
        return roleRepository.save(role);
    }

    /**
     * Update role
     */
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if name is being changed and if it's already taken
        if (!role.getName().equals(roleDetails.getName()) &&
            roleRepository.existsByName(roleDetails.getName())) {
            throw new RuntimeException("Role name already exists: " + roleDetails.getName());
        }

        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        role.setIsSystemRole(roleDetails.getIsSystemRole());

        return roleRepository.save(role);
    }

    /**
     * Delete role
     */
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (role.getIsSystemRole()) {
            throw new RuntimeException("Cannot delete system role: " + role.getName());
        }

        roleRepository.delete(role);
    }

    /**
     * Search roles by name
     */
    public List<Role> searchRolesByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search roles by name with pagination
     */
    public Page<Role> searchRolesByName(String name, Pageable pageable) {
        return roleRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    /**
     * Get roles with users
     */
    public List<Role> getRolesWithUsers() {
        return roleRepository.findRolesWithUsers();
    }

    /**
     * Get roles without users
     */
    public List<Role> getRolesWithoutUsers() {
        return roleRepository.findRolesWithoutUsers();
    }

    /**
     * Get roles by user ID
     */
    public List<Role> getRolesByUserId(Long userId) {
        return roleRepository.findByUserId(userId);
    }

    /**
     * Add permission to role
     */
    public Role addPermissionToRole(Long roleId, Permission permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        role.addPermission(permission);
        return roleRepository.save(role);
    }

    /**
     * Remove permission from role
     */
    public Role removePermissionFromRole(Long roleId, Permission permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        role.removePermission(permission);
        return roleRepository.save(role);
    }

    /**
     * Get permissions for role
     */
    public Set<Permission> getPermissionsForRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        return role.getPermissions();
    }

    /**
     * Count roles by system role flag
     */
    public long countBySystemRole(Boolean isSystemRole) {
        return roleRepository.countByIsSystemRole(isSystemRole);
    }

    /**
     * Check if role name exists
     */
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    /**
     * Get roles by permission
     */
    public List<Role> getRolesByPermission(String permissionName) {
        return roleRepository.findByPermissionName(permissionName);
    }
}