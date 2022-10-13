package com.example.onlineschoolapp.repository;

import com.example.onlineschoolapp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Optional<Student> getStudentByEmail(String email);

    @Query(value = "select * from Student s where max", nativeQuery = true)
    Integer getSmartestStudent();

    @Modifying
    @Transactional
    @Query("update Student s set s.firstName = ?2, s.lastName = ?3, s.email = ?4, s.age = ?5 where s.id = ?1")
    void updateById(long id, String firstName, String lastName, String email, double age);
}
