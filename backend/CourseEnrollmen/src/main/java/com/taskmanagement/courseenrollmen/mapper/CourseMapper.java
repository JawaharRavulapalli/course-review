package com.taskmanagement.courseenrollmen.mapper;

import com.taskmanagement.courseenrollmen.dto.CourseResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {

    public CourseResponseDTO toCourseResponseDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        courseResponseDTO.setId(course.getId());
        courseResponseDTO.setName(course.getName());
        courseResponseDTO.setDescription(course.getDescription());
        courseResponseDTO.setInstructor(course.getInstructor());
        return courseResponseDTO;
    }
}
