package com.simsekolah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCalendarResponse {

    private Long id;

    private String eventTitle;

    private String eventDescription;

    private LocalDate eventDate;

    private String eventType;

    private String academicYear;

    private Integer semester;

    private Boolean isHoliday;

    private Boolean isExamPeriod;

    private Boolean isTeachingPeriod;
}