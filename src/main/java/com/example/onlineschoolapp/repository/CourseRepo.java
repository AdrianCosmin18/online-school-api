package com.example.onlineschoolapp.repository;

import com.example.onlineschoolapp.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.name = :name")
    Optional<Course> getCourseByName(String name);

    @Query("delete from Course c where c.id = ?1")
    void deleteById(long id);

    Optional<List<Course>> getCoursesByDepartment(String department);

    @Query("update Course c set c.name = :name, c.department = :department where c.id = :id")
    void updateCourseById(String name, String department, long id);
}
