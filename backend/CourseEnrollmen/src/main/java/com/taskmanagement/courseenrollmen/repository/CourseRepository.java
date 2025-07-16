package com.taskmanagement.courseenrollmen.repository;

import com.taskmanagement.courseenrollmen.dto.CourseResponseDTO;
import com.taskmanagement.courseenrollmen.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


    @Query("SELECT new com.taskmanagement.courseenrollmen.dto.CourseResponseDTO(" +
            "c.id, c.name, c.description, COALESCE(AVG(r.rating), 0.0)) " +
            "FROM Course c LEFT JOIN c.reviews r " +
            "GROUP BY c.id, c.name, c.description")
    List<CourseResponseDTO> findAllWithAverageRatings();
}
