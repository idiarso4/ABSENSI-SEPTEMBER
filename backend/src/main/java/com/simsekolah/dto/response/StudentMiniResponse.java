package com.simsekolah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A minimal DTO for representing a student, typically for lists or selections.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMiniResponse {
    private Long id;
    private String nis;
    private String name;
}