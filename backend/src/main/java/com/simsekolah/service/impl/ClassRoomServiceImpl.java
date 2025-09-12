package com.simsekolah.service.impl;

import com.simsekolah.entity.ClassRoom;
import com.simsekolah.exception.ResourceNotFoundException;
import com.simsekolah.exception.ValidationException;
import com.simsekolah.repository.ClassRoomRepository;
import com.simsekolah.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Override
    public ClassRoom createClassRoom(ClassRoom classRoom) {
        // Validate class name uniqueness
        if (classRoomRepository.existsByName(classRoom.getName())) {
            throw new ValidationException("Class name already exists: " + classRoom.getName());
        }

        classRoom.setIsActive(true);
        return classRoomRepository.save(classRoom);
    }

    @Override
    public ClassRoom updateClassRoom(Long id, ClassRoom classRoomDetails) {
        ClassRoom classRoom = classRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found with id: " + id));

        // Check class name uniqueness if changed
        if (!classRoom.getName().equals(classRoomDetails.getName()) &&
            classRoomRepository.existsByName(classRoomDetails.getName())) {
            throw new ValidationException("Class name already exists: " + classRoomDetails.getName());
        }

        // Update fields
        classRoom.setName(classRoomDetails.getName());
        classRoom.setGrade(classRoomDetails.getGrade());
        classRoom.setAcademicYear(classRoomDetails.getAcademicYear());
        classRoom.setHomeroomTeacher(classRoomDetails.getHomeroomTeacher());
        classRoom.setCapacity(classRoomDetails.getCapacity());
        classRoom.setCurrentEnrollment(classRoomDetails.getCurrentEnrollment());
        classRoom.setIsActive(classRoomDetails.getIsActive());

        return classRoomRepository.save(classRoom);
    }

    @Override
    public Optional<ClassRoom> getClassRoomById(Long id) {
        return classRoomRepository.findById(id);
    }

    @Override
    public Page<ClassRoom> getAllClassRooms(Pageable pageable) {
        return classRoomRepository.findAll(pageable);
    }

    @Override
    public Page<ClassRoom> searchClassRooms(String query, Pageable pageable) {
        return classRoomRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    @Override
    public Page<ClassRoom> getClassRoomsByGrade(Integer grade, Pageable pageable) {
        return classRoomRepository.findByGrade(grade, pageable);
    }

    @Override
    public Page<ClassRoom> getActiveClassRooms(Pageable pageable) {
        return classRoomRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public List<ClassRoom> getClassRoomsWithAvailableSpace() {
        return classRoomRepository.findClassRoomsWithAvailableSpace();
    }

    @Override
    public List<ClassRoom> getFullClassRooms() {
        return classRoomRepository.findFullClassRooms();
    }

    @Override
    public void deleteClassRoom(Long id) {
        ClassRoom classRoom = classRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found with id: " + id));

        // Soft delete - set isActive to false
        classRoom.setIsActive(false);
        classRoomRepository.save(classRoom);
    }

    @Override
    public Map<String, Object> getClassRoomStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", classRoomRepository.count());
        stats.put("activeCount", classRoomRepository.countByIsActiveTrue());
        stats.put("availableSpaceCount", getClassRoomsWithAvailableSpace().size());
        stats.put("fullClassRoomsCount", getFullClassRooms().size());
        stats.put("timestamp", System.currentTimeMillis());
        return stats;
    }

    @Override
    public long countByGrade(Integer grade) {
        return classRoomRepository.countByGrade(grade);
    }

    @Override
    public long countByIsActiveTrue() {
        return classRoomRepository.countByIsActiveTrue();
    }

    @Override
    public boolean hasAvailableSpace(Long classRoomId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found with id: " + classRoomId));
        return classRoom.getCurrentEnrollment() < classRoom.getCapacity();
    }

    @Override
    public int getAvailableSeats(Long classRoomId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found with id: " + classRoomId));
        return classRoom.getCapacity() - classRoom.getCurrentEnrollment();
    }
}