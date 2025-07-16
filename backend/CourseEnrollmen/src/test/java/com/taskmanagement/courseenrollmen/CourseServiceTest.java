package com.taskmanagement.courseenrollmen;

import com.taskmanagement.courseenrollmen.dto.CourseResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import com.taskmanagement.courseenrollmen.exceptions.ResourceNotFoundException;
import com.taskmanagement.courseenrollmen.mapper.CourseMapper;
import com.taskmanagement.courseenrollmen.repository.CourseRepository;
import com.taskmanagement.courseenrollmen.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        course = new Course();
        course.setId(1L);
        course.setName("Math");
        course.setInstructor("Dr. Smith");
        course.setDescription("Basic math");

        responseDTO = new CourseResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Math");
        responseDTO.setInstructor("Dr. Smith");
        responseDTO.setDescription("Basic math");
    }


    @Test
    void getAllCoursesWithAverageRating_shouldReturnCourses() {
        when(courseRepository.findAllWithAverageRatings()).thenReturn(List.of(responseDTO));

        List<CourseResponseDTO> result = courseService.getAllCoursesWithAverageRating();

        assertEquals(1, result.size());
        assertEquals("Math", result.get(0).getName());
    }

    @Test
    void getAllCoursesWithAverageRating_shouldReturnEmptyListWhenNoCourses() {
        when(courseRepository.findAllWithAverageRatings()).thenReturn(List.of());

        List<CourseResponseDTO> result = courseService.getAllCoursesWithAverageRating();

        assertTrue(result.isEmpty());
    }


    @Test
    void getCourseById_shouldReturnCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toCourseResponseDTO(course)).thenReturn(responseDTO);

        CourseResponseDTO result = courseService.getCourseById(1L);

        assertEquals("Math", result.getName());
    }

    @Test
    void getCourseById_shouldThrowException_whenCourseNotFound() {
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(2L));
    }


    @Test
    void createCourse_shouldSaveAndReturnDTO() {
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toCourseResponseDTO(course)).thenReturn(responseDTO);

        CourseResponseDTO result = courseService.createCourse(course);

        assertEquals("Math", result.getName());
    }

    @Test
    void createCourse_shouldSaveCourse_withNullId() {
        course.setId(null);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toCourseResponseDTO(course)).thenReturn(responseDTO);

        CourseResponseDTO result = courseService.createCourse(course);

        assertEquals("Math", result.getName());
    }


    @Test
    void updateCourse_shouldUpdateAndReturnDTO() {
        Course updated = new Course();
        updated.setName("New");
        updated.setDescription("New Desc");
        updated.setInstructor("New Instructor");

        Course saved = new Course();
        saved.setId(1L);
        saved.setName("New");
        saved.setDescription("New Desc");
        saved.setInstructor("New Instructor");

        CourseResponseDTO updatedDTO = new CourseResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("New");
        updatedDTO.setDescription("New Desc");
        updatedDTO.setInstructor("New Instructor");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(saved);
        when(courseMapper.toCourseResponseDTO(saved)).thenReturn(updatedDTO);

        CourseResponseDTO result = courseService.updateCourse(1L, updated);

        assertEquals("New", result.getName());
        assertEquals("New Instructor", result.getInstructor());
    }

    @Test
    void updateCourse_shouldThrow_whenNotFound() {
        when(courseRepository.findById(10L)).thenReturn(Optional.empty());

        Course newData = new Course();
        newData.setName("X");

        assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(10L, newData));
    }


    @Test
    void deleteCourse_shouldDeleteIfExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));
        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourse_shouldThrow_whenNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.deleteCourse(1L));
    }
}
