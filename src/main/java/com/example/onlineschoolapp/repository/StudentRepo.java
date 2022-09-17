package com.example.onlineschoolapp.repository;

import com.example.onlineschoolapp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Optional<Student> getStudentByEmail(String email);
}
