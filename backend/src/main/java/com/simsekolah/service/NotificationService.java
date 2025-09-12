package com.simsekolah.service;

import com.simsekolah.entity.Attendance;
import com.simsekolah.enums.AttendanceStatus;
import com.simsekolah.entity.Student;
import com.simsekolah.entity.User;
import com.simsekolah.repository.AttendanceRepository;
import com.simsekolah.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final EmailService emailService; // Assuming an EmailService exists

    /**
     * Finds students who have been absent for a specified number of consecutive days
     * and sends a notification to their homeroom teacher.
     *
     * @param consecutiveDaysThreshold The number of consecutive days of absence to trigger a notification.
     */
    @Transactional(readOnly = true)
    public void notifyForConsecutiveAbsences(int consecutiveDaysThreshold) {
        log.info("Checking for students with {} consecutive absences.", consecutiveDaysThreshold);

        List<Student> activeStudents = studentRepository.findByStatus(com.simsekolah.enums.StudentStatus.ACTIVE);
        LocalDate today = LocalDate.now();

        for (Student student : activeStudents) {
            boolean isConsecutivelyAbsent = true;
            for (int i = 0; i < consecutiveDaysThreshold; i++) {
                LocalDate checkDate = today.minusDays(i);
                // This logic assumes one attendance record per day.
                // A more robust solution would check if ANY attendance on that day is PRESENT.
                List<Attendance> attendancesOnDate = attendanceRepository.findByStudentAndDate(student, checkDate);

                // If no record, or if any record is not ABSENT, the streak is broken.
                if (attendancesOnDate.isEmpty() || attendancesOnDate.stream().anyMatch(a -> a.getStatus() != AttendanceStatus.ABSENT)) {
                    isConsecutivelyAbsent = false;
                    break;
                }
            }

            if (isConsecutivelyAbsent) {
                sendConsecutiveAbsenceNotification(student, consecutiveDaysThreshold);
            }
        }
    }

    /**
     * Sends the actual notification.
     * Currently logs to the console, but can be extended to send emails, push notifications, etc.
     */
    private void sendConsecutiveAbsenceNotification(Student student, int days) {
        User homeroomTeacher = student.getClassRoom() != null ? student.getClassRoom().getWaliKelas() : null;

        if (homeroomTeacher != null) {
            String subject = String.format("Pemberitahuan Absensi: %s", student.getNamaLengkap());
            String message = String.format("Siswa %s (NIS: %s) dari kelas %s telah tercatat ALPA selama %d hari berturut-turut (hingga tanggal %s). Mohon untuk segera ditindaklanjuti.",
                    student.getNamaLengkap(), student.getNis(), student.getClassRoom().getName(), days, LocalDate.now());

            log.warn("NOTIFICATION TO: {} <{}> | SUBJECT: {} | MESSAGE: {}", homeroomTeacher.getFullName(), homeroomTeacher.getEmail(), subject, message);
            // emailService.sendNotificationEmail(homeroomTeacher, subject, message);
        } else {
            log.warn("Cannot send absence notification for student {} (ID: {}). No homeroom teacher (wali kelas) is assigned to their class.", student.getNamaLengkap(), student.getId());
        }
    }
}