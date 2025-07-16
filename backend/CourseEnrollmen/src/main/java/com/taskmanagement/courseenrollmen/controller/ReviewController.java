package com.taskmanagement.courseenrollmen.controller;

import com.taskmanagement.courseenrollmen.dto.ReviewRequestDTO;
import com.taskmanagement.courseenrollmen.dto.ReviewResponseDTO;
import com.taskmanagement.courseenrollmen.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/api/courses/{courseId}/reviews")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @PathVariable Long courseId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewResponseDTO response = reviewService.createReview(courseId, reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/courses/{courseId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByCourse(@PathVariable Long courseId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByCourseId(courseId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long reviewId) {
        ReviewResponseDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewResponseDTO updated = reviewService.updateReview(reviewId, reviewRequestDTO);
        return ResponseEntity.ok(updated);
    }


}
