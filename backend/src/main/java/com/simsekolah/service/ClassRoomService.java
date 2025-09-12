package com.simsekolah.service;

import com.simsekolah.entity.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClassRoomService {

    ClassRoom createClassRoom(ClassRoom classRoom);

    ClassRoom updateClassRoom(Long id, ClassRoom classRoomDetails);

    Optional<ClassRoom> getClassRoomById(Long id);

    Page<ClassRoom> getAllClassRooms(Pageable pageable);

    Page<ClassRoom> searchClassRooms(String query, Pageable pageable);

    Page<ClassRoom> getClassRoomsByGrade(Integer grade, Pageable pageable);

    Page<ClassRoom> getActiveClassRooms(Pageable pageable);

    List<ClassRoom> getClassRoomsWithAvailableSpace();

    List<ClassRoom> getFullClassRooms();

    void deleteClassRoom(Long id);

    Map<String, Object> getClassRoomStatistics();

    long countByGrade(Integer grade);

    long countByIsActiveTrue();

    boolean hasAvailableSpace(Long classRoomId);

    int getAvailableSeats(Long classRoomId);
}
