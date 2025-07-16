package com.taskmanagement.courseenrollmen.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StarRatingValidator implements ConstraintValidator<StarRating, Integer> {
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 1 && value <= 5;
    }
}
