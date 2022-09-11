package com.example.onlineschoolapp;

import com.example.onlineschoolapp.models.Book;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.models.Student;
import com.example.onlineschoolapp.repository.BookRepo;
import com.example.onlineschoolapp.repository.CourseRepo;
import com.example.onlineschoolapp.repository.StudentRepo;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineSchoolAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineSchoolAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepo studentRepo, CourseRepo courseRepo, BookRepo bookRepo){

        return args -> {

//            Student student = studentRepo.getReferenceById(1L);
//            Course course = courseRepo.getReferenceById(1L);

//            student.addCourse(course);
//
//            studentRepo.save(student);

//            System.out.println(student);
//            System.out.println(course);
        };
    }

}
