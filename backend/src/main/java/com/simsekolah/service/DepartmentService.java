package com.simsekolah.service;

import com.simsekolah.entity.Department;
import com.simsekolah.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Department operations
 */
@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Get all active departments
     */
    public List<Department> getAllActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }

    /**
     * Get all active departments with pagination
     */
    public Page<Department> getAllActiveDepartments(Pageable pageable) {
        return departmentRepository.findByIsActiveTrue(pageable);
    }

    /**
     * Get department by ID
     */
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    /**
     * Get department by code
     */
    public Optional<Department> getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code);
    }

    /**
     * Get department by name
     */
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    /**
     * Create new department
     */
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new RuntimeException("Department code already exists: " + department.getCode());
        }
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department name already exists: " + department.getName());
        }
        return departmentRepository.save(department);
    }

    /**
     * Update department
     */
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        // Check if code is being changed and if it's already taken
        if (!department.getCode().equals(departmentDetails.getCode()) &&
            departmentRepository.existsByCode(departmentDetails.getCode())) {
            throw new RuntimeException("Department code already exists: " + departmentDetails.getCode());
        }

        // Check if name is being changed and if it's already taken
        if (!department.getName().equals(departmentDetails.getName()) &&
            departmentRepository.existsByName(departmentDetails.getName())) {
            throw new RuntimeException("Department name already exists: " + departmentDetails.getName());
        }

        department.setCode(departmentDetails.getCode());
        department.setName(departmentDetails.getName());
        department.setDescription(departmentDetails.getDescription());
        department.setIsActive(departmentDetails.getIsActive());

        return departmentRepository.save(department);
    }

    /**
     * Delete department (soft delete by setting isActive to false)
     */
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        department.setIsActive(false);
        departmentRepository.save(department);
    }

    /**
     * Search departments by name
     */
    public List<Department> searchDepartmentsByName(String searchTerm) {
        return departmentRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(searchTerm);
    }

    /**
     * Search departments by name with pagination
     */
    public Page<Department> searchDepartmentsByName(String searchTerm, Pageable pageable) {
        return departmentRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(searchTerm, pageable);
    }

    /**
     * Get departments with major count
     */
    public List<Object[]> getDepartmentsWithMajorCount() {
        return departmentRepository.findDepartmentsWithMajorCount();
    }

    /**
     * Get departments that have majors
     */
    public List<Department> getDepartmentsWithMajors() {
        return departmentRepository.findDepartmentsWithMajors();
    }

    /**
     * Get departments without majors
     */
    public List<Department> getDepartmentsWithoutMajors() {
        return departmentRepository.findDepartmentsWithoutMajors();
    }

    /**
     * Count active departments
     */
    public long countActiveDepartments() {
        return departmentRepository.countByIsActiveTrue();
    }

    /**
     * Check if department code exists
     */
    public boolean existsByCode(String code) {
        return departmentRepository.existsByCode(code);
    }

    /**
     * Check if department name exists
     */
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}