package com.taskmanagement.courseenrollmen.dto;


import com.taskmanagement.courseenrollmen.validation.StarRating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

    @StarRating
    private Integer rating;

    @NotBlank
    private String comment;

    @NotNull
    private Long userId;
}
