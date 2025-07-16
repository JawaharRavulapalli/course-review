package com.taskmanagement.courseenrollmen.mapper;

import com.taskmanagement.courseenrollmen.dto.ReviewRequestDTO;
import com.taskmanagement.courseenrollmen.dto.ReviewResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import com.taskmanagement.courseenrollmen.entity.Review;
import com.taskmanagement.courseenrollmen.entity.User;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {
    public Review toReviewEntity(ReviewRequestDTO requestDTO, Course course, User user) {
        Review review = new Review();
        review.setRating(requestDTO.getRating());
        review.setComment(requestDTO.getComment());
        review.setCourse(course);
        review.setUser(user);
        return review;
    }

    public ReviewResponseDTO toReviewResponseDto(Review review) {
        if (review == null || review.getUser() == null) {
            return null;
        }
        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getUser().getName()
        );
    }
}
