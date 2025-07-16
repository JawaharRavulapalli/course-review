package com.taskmanagement.courseenrollmen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String instructor;
    private Double averageRating;


    public CourseResponseDTO(Long id, String name, String description, Double averageRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
    }
}