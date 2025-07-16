package com.taskmanagement.courseenrollmen.service;

import com.taskmanagement.courseenrollmen.dto.ReviewRequestDTO;
import com.taskmanagement.courseenrollmen.dto.ReviewResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import com.taskmanagement.courseenrollmen.entity.Review;
import com.taskmanagement.courseenrollmen.entity.User;
import com.taskmanagement.courseenrollmen.exceptions.ResourceNotFoundException;
import com.taskmanagement.courseenrollmen.mapper.ReviewMapper;
import com.taskmanagement.courseenrollmen.repository.CourseRepository;
import com.taskmanagement.courseenrollmen.repository.ReviewRepository;
import com.taskmanagement.courseenrollmen.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         CourseRepository courseRepository,
                         UserRepository userRepository,
                         ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewResponseDTO createReview(Long courseId, @Valid ReviewRequestDTO reviewRequestDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        User user = userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + reviewRequestDTO.getUserId()));

        Review review = reviewMapper.toReviewEntity(reviewRequestDTO, course, user);

        Review saved = reviewRepository.save(review);

        return reviewMapper.toReviewResponseDto(saved);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        List<Review> reviews = reviewRepository.findByCourse(course);

        return reviews.stream()
                .map(reviewMapper::toReviewResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        return reviewMapper.toReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, @Valid ReviewRequestDTO reviewRequestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());

        Review updated = reviewRepository.save(review);

        return reviewMapper.toReviewResponseDto(updated);
    }
}
