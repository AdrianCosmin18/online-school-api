package com.example.onlineschoolapp.repository;

import com.example.onlineschoolapp.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {


    Optional<Course> getCourseByName(String name);

    Optional<List<Course>> getCoursesByDepartment(String department);

    @Modifying
    @Transactional
    @Query("update Course c set c.name = ?1, c.department = ?2 where c.id = ?3")
    void updateCourseById(String name, String department, long id);
}
