package com.simsekolah.repository;

import com.simsekolah.entity.Student;
import com.simsekolah.enums.StudentStatus;
import com.simsekolah.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByClassRoomId(Long classRoomId);

    Optional<Student> findByNis(String nis);

    List<Student> findByStatus(StudentStatus status);

    Optional<Student> findByUserId(Long userId);

    boolean existsByNis(String nis);

    @Query("SELECT s FROM Student s WHERE s.classRoom.id = :classRoomId AND s.status = :status")
    List<Student> findByClassRoomIdAndStatus(@Param("classRoomId") Long classRoomId,
                                           @Param("status") StudentStatus status);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.classRoom.id = :classRoomId")
    long countByClassRoomId(@Param("classRoomId") Long classRoomId);

    @Query("SELECT s FROM Student s WHERE s.status = 'ACTIVE' ORDER BY s.namaLengkap")
    List<Student> findAllActiveStudents();

    @Query("SELECT s FROM Student s WHERE s.classRoom.id = :classRoomId AND s.status = 'ACTIVE'")
    List<Student> findActiveStudentsByClassRoom(@Param("classRoomId") Long classRoomId);
    
    // Added missing methods
    Page<Student> findByClassRoomId(Long classRoomId, Pageable pageable);
    
    Page<Student> findByStatus(StudentStatus status, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.classRoom.major.id = :majorId")
    Page<Student> findByMajorId(@Param("majorId") Long majorId, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.classRoom.gradeLevel = :grade")
    Page<Student> findByGrade(@Param("grade") Integer grade, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.tahunMasuk = :academicYear")
    Page<Student> findByAcademicYear(@Param("academicYear") String academicYear, Pageable pageable);
    
    @Query("SELECT status as status, COUNT(s) as count FROM Student s GROUP BY status")
    List<Object[]> getStudentStatisticsByStatus();
    
       @Query("SELECT c.gradeLevel as grade, COUNT(s) as count FROM Student s JOIN s.classRoom c GROUP BY c.gradeLevel")
       List<Object[]> getStudentStatisticsByGrade();
    
    @Query("SELECT m.name as majorName, COUNT(s) as count FROM Student s JOIN s.classRoom.major m GROUP BY m.name")
    List<Object[]> getStudentStatisticsByMajor();
    
    @Query("SELECT s FROM Student s WHERE s.classRoom IS NULL")
    List<Student> findByClassRoomIsNull();
    
    @Query("SELECT s FROM Student s WHERE s.user IS NULL")
    List<Student> findStudentsWithoutUserAccount();
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.classRoom.id = :classRoomId AND s.status = :status")
    long countByClassRoomIdAndStatus(@Param("classRoomId") Long classRoomId, @Param("status") StudentStatus status);
    
    @Query(value = "SELECT * FROM students s WHERE YEAR(s.tanggal_lahir) = :birthYear", nativeQuery = true)
    List<Student> findByBirthYear(@Param("birthYear") Integer birthYear);
    
    @Query(value = "SELECT * FROM students s WHERE TIMESTAMPDIFF(YEAR, s.tanggal_lahir, CURDATE()) BETWEEN :minAge AND :maxAge", nativeQuery = true)
    List<Student> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    List<Student> findByJenisKelamin(Gender jenisKelamin);
    
    @Query("SELECT s FROM Student s WHERE s.agama = :agama")
    List<Student> findByAgama(@Param("agama") String agama);
    
    @Query("SELECT s FROM Student s WHERE s.asalSekolah = :asalSekolah")
    List<Student> findByAsalSekolah(@Param("asalSekolah") String asalSekolah);
    
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(s.namaLengkap) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:nis IS NULL OR s.nis = :nis) AND " +
           "(:classRoomId IS NULL OR s.classRoom.id = :classRoomId) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:tahunMasuk IS NULL OR s.tahunMasuk = :tahunMasuk) AND " +
           "(:agama IS NULL OR s.agama = :agama) AND " +
           "(:asalSekolah IS NULL OR s.asalSekolah = :asalSekolah) AND " +
           "(:parentName IS NULL OR LOWER(s.namaAyah) LIKE LOWER(CONCAT('%', :parentName, '%')) OR LOWER(s.namaIbu) LIKE LOWER(CONCAT('%', :parentName, '%')))")
    Page<Student> advancedSearchStudents(@Param("name") String name,
                                       @Param("nis") String nis,
                                       @Param("classRoomId") Long classRoomId,
                                       @Param("status") StudentStatus status,
                                       @Param("tahunMasuk") Integer tahunMasuk,
                                       @Param("agama") String agama,
                                       @Param("asalSekolah") String asalSekolah,
                                       @Param("parentName") String parentName,
                                       Pageable pageable);
}
