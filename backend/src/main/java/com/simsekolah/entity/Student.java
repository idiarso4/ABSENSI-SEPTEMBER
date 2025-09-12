package com.simsekolah.entity;

import com.simsekolah.enums.Gender;
import com.simsekolah.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nis", unique = true)
    private String nis;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "tempat_lahir")
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private Gender jenisKelamin;

    @Column(name = "agama")
    private String agama;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "nama_ayah")
    private String namaAyah;

    @Column(name = "nama_ibu")
    private String namaIbu;

    @Column(name = "pekerjaan_ayah")
    private String pekerjaanAyah;

    @Column(name = "pekerjaan_ibu")
    private String pekerjaanIbu;

    @Column(name = "no_hp_ortu")
    private String noHpOrtu;

    @Column(name = "alamat_ortu", columnDefinition = "TEXT")
    private String alamatOrtu;

    @Column(name = "phone")
    private String phone;

    @Column(name = "tahun_masuk")
    private Integer tahunMasuk;

    @Column(name = "asal_sekolah")
    private String asalSekolah;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StudentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assessment> assessments;

    @ManyToMany
    @JoinTable(
        name = "student_extracurriculars",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<ExtracurricularActivity> extracurricularActivities;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Legacy fields for backward compatibility
    private String name;
    private String email;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public String getFullName() {
        return namaLengkap != null ? namaLengkap : name;
    }

    public boolean isActive() {
        return status == StudentStatus.ACTIVE;
    }

    public boolean isGraduated() {
        return status == StudentStatus.GRADUATED;
    }

    public String getClassRoomName() {
        return classRoom != null ? classRoom.getClassName() : null;
    }

    public String getUserEmail() {
        return user != null ? user.getEmail() : email;
    }

    public String getClassName() {
        return classRoom != null ? classRoom.getClassName() : null;
    }
}
