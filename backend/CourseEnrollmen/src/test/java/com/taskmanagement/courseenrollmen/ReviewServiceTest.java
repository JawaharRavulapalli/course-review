package com.taskmanagement.courseenrollmen;

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

import com.taskmanagement.courseenrollmen.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private UserRepository userRepository;
    @Mock private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;


    @Test
    void createReview_success() {
        Long courseId = 1L;
        Long userId = 2L;

        Course course = new Course();
        course.setId(courseId);

        User user = new User();
        user.setId(userId);

        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setRating(5);
        dto.setComment("Great");
        dto.setUserId(userId);

        Review reviewEntity = new Review();
        Review savedEntity = new Review();
        savedEntity.setId(100L);

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setReviewId(100L);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reviewMapper.toReviewEntity(dto, course, user)).thenReturn(reviewEntity);
        when(reviewRepository.save(reviewEntity)).thenReturn(savedEntity);
        when(reviewMapper.toReviewResponseDto(savedEntity)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.createReview(courseId, dto);

        assertEquals(100L, result.getReviewId());
        verify(reviewRepository).save(reviewEntity);
    }

    @Test
    void createReview_courseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setUserId(2L);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.createReview(99L, dto));
    }

    @Test
    void deleteReview_success() {
        Review review = new Review();
        review.setId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertDoesNotThrow(() -> reviewService.deleteReview(1L));
        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteReview_notFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(99L));
    }

    @Test
    void getReviewsByCourseId_success() {
        Course course = new Course();
        course.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setCourse(course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(reviewRepository.findByCourse(course)).thenReturn(List.of(review));

        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(1L);
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(dto);

        List<ReviewResponseDTO> result = reviewService.getReviewsByCourseId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getReviewsByCourseId_courseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewsByCourseId(1L));
    }

    @Test
    void getReviewById_success() {
        Review review = new Review();
        review.setId(5L);

        when(reviewRepository.findById(5L)).thenReturn(Optional.of(review));

        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(5L);

        when(reviewMapper.toReviewResponseDto(review)).thenReturn(dto);

        ReviewResponseDTO result = reviewService.getReviewById(5L);

        assertEquals(5L, result.getReviewId());
    }

    @Test
    void getReviewById_notFound() {
        when(reviewRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewById(123L));
    }

    @Test
    void updateReview_success() {
        Review review = new Review();
        review.setId(1L);
        review.setRating(3);
        review.setComment("Old comment");

        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setRating(5);
        dto.setComment("New comment");

        Review updatedReview = new Review();
        updatedReview.setId(1L);
        updatedReview.setRating(5);
        updatedReview.setComment("New comment");

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setReviewId(1L);
        response.setComment("New comment");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(updatedReview);
        when(reviewMapper.toReviewResponseDto(updatedReview)).thenReturn(response);

        ReviewResponseDTO result = reviewService.updateReview(1L, dto);

        assertEquals("New comment", result.getComment());
    }

    @Test
    void updateReview_notFound() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setComment("Update");
        dto.setRating(4);

        when(reviewRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReview(100L, dto));
    }
}
