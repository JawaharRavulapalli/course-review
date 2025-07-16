package com.taskmanagement.courseenrollmen.service;

import com.taskmanagement.courseenrollmen.dto.CourseResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import com.taskmanagement.courseenrollmen.exceptions.ResourceNotFoundException;
import com.taskmanagement.courseenrollmen.mapper.CourseMapper;
import com.taskmanagement.courseenrollmen.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }


    public List<CourseResponseDTO> getAllCoursesWithAverageRating() {
        return courseRepository.findAllWithAverageRatings();
    }

    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseById(Long id) {
        Course course= courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        return courseMapper.toCourseResponseDTO(course);
    }

    @Transactional
    public CourseResponseDTO createCourse(@Valid Course course) {
        return courseMapper.toCourseResponseDTO(courseRepository.save(course));
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long id, Course updatedCourse) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        existing.setName(updatedCourse.getName());
        existing.setDescription(updatedCourse.getDescription());
        existing.setInstructor(updatedCourse.getInstructor());

        return courseMapper.toCourseResponseDTO(courseRepository.save(existing));
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        courseRepository.delete(course);
    }
}
