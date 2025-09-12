package com.simsekolah.repository;

import com.simsekolah.entity.User;
import com.simsekolah.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByNip(String nip);

    List<User> findByUserType(UserType userType);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNip(String nip);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.userType = :userType")
    List<User> findActiveUsersByType(@Param("userType") UserType userType);

    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType AND u.isActive = true")
    long countActiveUsersByType(@Param("userType") UserType userType);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles r WHERE u.email = :identifier OR u.nip = :identifier OR u.username = :identifier")
    Optional<User> findByEmailOrNipForAuthentication(@Param("identifier") String identifier);

    // Pagination variants used by services
    Page<User> findByUserType(UserType userType, Pageable pageable);
    Page<User> findByUserTypeAndIsActive(UserType userType, boolean isActive, Pageable pageable);

    @Query(value = "SELECT u FROM User u WHERE u.userType = :userType AND (" +
                 "LOWER(CONCAT(COALESCE(u.firstName,''),' ',COALESCE(u.lastName,''))) LIKE LOWER(CONCAT('%',:query,'%')) OR " +
                 "LOWER(u.username) LIKE LOWER(CONCAT('%',:query,'%')) OR " +
                 "LOWER(u.email) LIKE LOWER(CONCAT('%',:query,'%')))",
           countQuery = "SELECT COUNT(u) FROM User u WHERE u.userType = :userType AND (" +
                       "LOWER(CONCAT(COALESCE(u.firstName,''),' ',COALESCE(u.lastName,''))) LIKE LOWER(CONCAT('%',:query,'%')) OR " +
                       "LOWER(u.username) LIKE LOWER(CONCAT('%',:query,'%')) OR " +
                       "LOWER(u.email) LIKE LOWER(CONCAT('%',:query,'%')))")
    Page<User> findByUserTypeAndQuery(@Param("userType") UserType userType,
                                      @Param("query") String query,
                                      Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE UPPER(r.name) = UPPER(:roleName)")
    List<User> findByRoleName(@Param("roleName") String roleName);

    long countByIsActiveTrue();

    long countByUserType(UserType userType);

    long countByUserTypeAndIsActive(UserType userType, boolean isActive);

    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType AND u.createdAt > :createdAt")
    long countByUserTypeAndCreatedAtAfter(@Param("userType") UserType userType, @Param("createdAt") LocalDateTime createdAt);

    @Query("SELECT u FROM User u WHERE (" +
           ":nameQuery IS NULL OR LOWER(CONCAT(COALESCE(u.firstName,''),' ',COALESCE(u.lastName,''))) LIKE LOWER(CONCAT('%',:nameQuery,'%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%',:nameQuery,'%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%',:nameQuery,'%'))) AND (" +
           ":userType IS NULL OR u.userType = :userType) AND (" +
           ":isActive IS NULL OR u.isActive = :isActive)")
    Page<User> searchUsers(@Param("nameQuery") String nameQuery,
                           @Param("userType") UserType userType,
                           @Param("isActive") Boolean isActive,
                           Pageable pageable);
}
