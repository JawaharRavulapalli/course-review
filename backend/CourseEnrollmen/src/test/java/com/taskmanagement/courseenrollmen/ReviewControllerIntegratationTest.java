package com.taskmanagement.courseenrollmen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.courseenrollmen.controller.ReviewController;
import com.taskmanagement.courseenrollmen.dto.ReviewRequestDTO;
import com.taskmanagement.courseenrollmen.dto.ReviewResponseDTO;
import com.taskmanagement.courseenrollmen.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import(ReviewControllerIntegratationTest.MockConfig.class)
public class ReviewControllerIntegratationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewService reviewService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ReviewService reviewService() {
            return Mockito.mock(ReviewService.class);
        }
    }

    @Test
    public void createReview_success() throws Exception {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setComment("Great!");
        request.setRating(5);
        request.setUserId(10L);

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setReviewId(1L);
        response.setComment("Great!");
        response.setRating(5);


        Mockito.when(reviewService.createReview(eq(1L), any(ReviewRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/courses/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(1L))
                .andExpect(jsonPath("$.comment").value("Great!"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    public void createReview_validationFail() throws Exception {
        ReviewRequestDTO request = new ReviewRequestDTO(); // Invalid (missing fields)

        mockMvc.perform(post("/api/courses/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteReview_success() throws Exception {
        Mockito.doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteReview_notFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Not found")).when(reviewService).deleteReview(999L);

        mockMvc.perform(delete("/api/reviews/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getReviewsByCourse_success() throws Exception {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(1L);
        dto.setComment("Nice");
        dto.setRating(4);

        Mockito.when(reviewService.getReviewsByCourseId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/courses/1/reviews"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(1)))
                .andExpect((ResultMatcher) jsonPath("$[0].reviewId").value(1L))
                .andExpect((ResultMatcher) jsonPath("$[0].comment").value("Nice"))
                .andExpect((ResultMatcher) jsonPath("$[0].rating").value(4));
    }

    @Test
    public void getReviewsByCourse_emptyList() throws Exception {
        Mockito.when(reviewService.getReviewsByCourseId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/courses/1/reviews"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(0)));
    }

    @Test
    public void getReviewById_success() throws Exception {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(1L);
        dto.setComment("Helpful");
        dto.setRating(5);

        Mockito.when(reviewService.getReviewById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.reviewId").value(1L))
                .andExpect((ResultMatcher) jsonPath("$.comment").value("Helpful"))
                .andExpect((ResultMatcher) jsonPath("$.rating").value(5));
    }

    @Test
    public void getReviewById_notFound() throws Exception {
        Mockito.when(reviewService.getReviewById(999L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/reviews/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateReview_success() throws Exception {
        ReviewRequestDTO updateRequest = new ReviewRequestDTO();
        updateRequest.setComment("Updated!");
        updateRequest.setRating(3);
        updateRequest.setUserId(10L);

        ReviewResponseDTO updatedResponse = new ReviewResponseDTO();
        updatedResponse.setReviewId(1L);
        updatedResponse.setComment("Updated!");
        updatedResponse.setRating(3);

        Mockito.when(reviewService.updateReview(eq(1L), any(ReviewRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Updated!"))
                .andExpect(jsonPath("$.rating").value(3));
    }

    @Test
    public void updateReview_validationFail() throws Exception {
        ReviewRequestDTO invalid = new ReviewRequestDTO(); // Missing required fields

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }


}