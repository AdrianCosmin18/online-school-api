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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootApplication
public class OnlineSchoolAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineSchoolAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepo studentRepo, CourseRepo courseRepo, BookRepo bookRepo){

        return args -> {

//            Faker f = new Faker();
//            Book b1 = new Book(f.book().title(), LocalDate.parse("2021-09-15"));
//            Book b2 = new Book(f.book().title(), LocalDate.parse("19-09-2021", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//            Book b3 = new Book(f.book().title(), LocalDate.now());

//            Student s1 = studentRepo.findById(6L).get();
//            s1.addBook(b1);
//            s1.addBook(b2);
//            studentRepo.save(s1);

//            Student s2 = studentRepo.findById(5L).get();
//            s2.addBook(b3);
//            studentRepo.save(s2);

//            Student s1 = studentRepo.findById(6L).get();
//            Student s2 = studentRepo.findById(5L).get();
//            Course c1 = courseRepo.findById(1L).get();
//            Course c2 = courseRepo.findById(2L).get();
//
//            s1.addCourse(c1);
//            s1.addCourse(c2);
//            s2.addCourse(c1);
//            s2.addCourse(c2);
//
//
//            studentRepo.save(s1);
//            studentRepo.save(s2);



        };
    }

}
