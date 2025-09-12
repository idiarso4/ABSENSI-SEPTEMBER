package com.simsekolah.entity;

import com.simsekolah.enums.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "academic_calendar")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "academic_year")
    private String academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ElementCollection
    @CollectionTable(name = "academic_calendar_dates", joinColumns = @JoinColumn(name = "academic_calendar_id"))
    @Column(name = "event_date")
    private Set<LocalDateTime> eventDates;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Legacy getters for backward compatibility
    public String getEventType() {
        return eventType != null ? eventType.name() : null;
    }

    public void setEventType(String eventType) {
        try {
            this.eventType = EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.eventType = EventType.OTHER;
        }
    }

    // Helper methods
    public String getEventTitle() {
        return eventName;
    }

    public String getEventDescription() {
        return description;
    }

    public boolean getIsHoliday() {
        return eventType == EventType.HOLIDAY;
    }

    public boolean getIsExamPeriod() {
        return eventType == EventType.EXAM;
    }

    public boolean getIsTeachingPeriod() {
        return eventType == EventType.TEACHING_PERIOD;
    }

    // Helper method to get semester ID
    public Long getSemesterId() {
        return semester != null ? semester.getId() : null;
    }

    // Builder methods for backward compatibility
    public static class AcademicCalendarBuilder {
        public AcademicCalendarBuilder eventTitle(String eventTitle) {
            this.eventName = eventTitle;
            return this;
        }

        public AcademicCalendarBuilder eventDescription(String eventDescription) {
            this.description = eventDescription;
            return this;
        }

        public AcademicCalendarBuilder isHoliday(Boolean isHoliday) {
            if (isHoliday != null && isHoliday) {
                this.eventType = EventType.HOLIDAY;
            }
            return this;
        }

        public AcademicCalendarBuilder isExamPeriod(Boolean isExamPeriod) {
            if (isExamPeriod != null && isExamPeriod) {
                this.eventType = EventType.EXAM;
            }
            return this;
        }

        public AcademicCalendarBuilder isTeachingPeriod(Boolean isTeachingPeriod) {
            if (isTeachingPeriod != null && isTeachingPeriod) {
                this.eventType = EventType.TEACHING_PERIOD;
            }
            return this;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}