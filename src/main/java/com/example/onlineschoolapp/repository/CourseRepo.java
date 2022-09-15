package com.example.onlineschoolapp.repository;

import com.example.onlineschoolapp.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.name = :name")
    Optional<Course> getCourseByName(String name);
}
