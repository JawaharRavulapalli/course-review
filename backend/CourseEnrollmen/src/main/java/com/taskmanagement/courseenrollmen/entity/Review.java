package com.taskmanagement.courseenrollmen.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.taskmanagement.courseenrollmen.validation.StarRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-review")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "review-user")
    private User user;

    @StarRating
    private Integer rating;

    @NotBlank
    private String comment;
}
