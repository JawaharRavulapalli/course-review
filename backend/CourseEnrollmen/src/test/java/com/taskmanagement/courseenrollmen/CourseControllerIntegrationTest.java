package com.taskmanagement.courseenrollmen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskmanagement.courseenrollmen.controller.CourseController;
import com.taskmanagement.courseenrollmen.dto.CourseResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import com.taskmanagement.courseenrollmen.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(CourseControllerIntegrationTest.MockConfig.class)
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;


    @TestConfiguration
    static class MockConfig {
        @Bean
        CourseService courseService() {
            return Mockito.mock(CourseService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper;
        }
    }

    private CourseResponseDTO sampleCourseDTO;

    @BeforeEach
    public void setup() {
        sampleCourseDTO = new CourseResponseDTO();
        sampleCourseDTO.setId(1L);
        sampleCourseDTO.setName("React Fundamentals");
        sampleCourseDTO.setDescription("Learn React from scratch");
        sampleCourseDTO.setInstructor("John Smith");
        sampleCourseDTO.setAverageRating(4.5);
    }

    @Test
    @DisplayName("GET /api/courses/ should return empty list")
    void getAllCourses_empty() throws Exception {
        when(courseService.getAllCoursesWithAverageRating()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/courses/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void getAllCourses_success() throws Exception {
        List<CourseResponseDTO> courses = List.of(sampleCourseDTO);

        when(courseService.getAllCoursesWithAverageRating()).thenReturn(courses);

        mockMvc.perform(get("/api/courses/")
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("React Fundamentals")));
    }

    @Test
    public void getCourseById_success() throws Exception {
        Mockito.when(courseService.getCourseById(1L)).thenReturn(sampleCourseDTO);

        mockMvc.perform(get("/api/courses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("React Fundamentals")));
    }

    @Test
    @DisplayName("GET /api/courses/{id} should return 404")
    void getCourseById_notFound() throws Exception {
        when(courseService.getCourseById(99L)).thenThrow(new RuntimeException("Course not found"));

        mockMvc.perform(get("/api/courses/99"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void createCourse_success() throws Exception {
        Course newCourse = new Course();
        newCourse.setName("React Fundamentals");
        newCourse.setDescription("Learn React from scratch");
        newCourse.setInstructor("John Smith");

        Mockito.when(courseService.createCourse(Mockito.any(Course.class))).thenReturn(sampleCourseDTO);

        mockMvc.perform(post("/api/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("React Fundamentals")))
                .andExpect(jsonPath("$.averageRating", is(4.5)));
    }


    @Test
    @DisplayName("POST /api/courses/ with invalid body should fail")
    void createCourse_invalid() throws Exception {
        Course course = new Course(); // missing required fields

        mockMvc.perform(post("/api/courses/")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCourse_success() throws Exception {
        Course updatedCourse = new Course();
        updatedCourse.setName("React Advanced");
        updatedCourse.setDescription("Advanced React Topics");
        updatedCourse.setInstructor("Jane Doe");

        CourseResponseDTO updatedDTO = new CourseResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("React Advanced");
        updatedDTO.setDescription("Advanced React Topics");
        updatedDTO.setInstructor("Jane Doe");
        updatedDTO.setAverageRating(4.7);

        Mockito.when(courseService.updateCourse(Mockito.eq(1L), Mockito.any(Course.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("React Advanced")))
                .andExpect(jsonPath("$.averageRating", is(4.7)));
    }

    @Test
    @DisplayName("PUT /api/courses/{id} invalid body should fail")
    void updateCourse_invalid() throws Exception {
        mockMvc.perform(put("/api/courses/1")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCourse_success() throws Exception {
        Mockito.doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/courses/{id} with exception")
    public void deleteCourse_error() throws Exception {
        Mockito.doThrow(new RuntimeException("Cannot delete"))
                .when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Cannot delete")))
                .andExpect(jsonPath("$.status", is(500)));
    }
}