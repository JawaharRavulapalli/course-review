package com.taskmanagement.courseenrollmen.controller;

import com.taskmanagement.courseenrollmen.dto.CourseResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import com.taskmanagement.courseenrollmen.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCoursesWithAverageRating();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        CourseResponseDTO courseResponseDTO = courseService.getCourseById(id);
        return ResponseEntity.ok(courseResponseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody Course course) {
        CourseResponseDTO courseResponseDTO = courseService.createCourse(course);
        return ResponseEntity.status(201).body(courseResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody Course updatedCourse) {
        CourseResponseDTO courseResponseDTO = courseService.updateCourse(id, updatedCourse);
        return ResponseEntity.ok(courseResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
