package com.example.onlineschoolapp.controller;


import com.example.onlineschoolapp.dto.BookDTO;
import com.example.onlineschoolapp.dto.StudentDTO;
import com.example.onlineschoolapp.models.Book;
import com.example.onlineschoolapp.models.Student;
import com.example.onlineschoolapp.services.StudentAndBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("online-school/api/v1/students")
public class StudentController {

    private StudentAndBookService service;

    public StudentController(StudentAndBookService service) {
        this.service = service;
    }

    @GetMapping("/all-students")
    public ResponseEntity<List<Student>> getStudents(){
        return new ResponseEntity<List<Student>>(this.service.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/get-student-by-id/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable long id){
        return new ResponseEntity<Student>(this.service.getStudentById(id), HttpStatus.OK);
    }

    @GetMapping("/all-books")
    public ResponseEntity<List<Book>> getBooks(){
        return new ResponseEntity<List<Book>>(this.service.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/get-book-by-id/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id){
        return new ResponseEntity<Book>(this.service.getBookById(id), HttpStatus.OK);
    }

    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestBody StudentDTO s){
        this.service.addStudent(s);
        return new ResponseEntity<>("student added", HttpStatus.CREATED);
    }

    @PostMapping("/add-book-to-student/{id}")
    public ResponseEntity<String> addBookToStudent(@PathVariable long id, @RequestBody BookDTO book){
        this.service.addBookToStudent(id, book);
        return new ResponseEntity<>("book added to student", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-book-from-student/{studentId}/{bookId}")
    public ResponseEntity<String> deleteBookFromStudent(@PathVariable long studentId, @PathVariable long bookId){
        this.service.deleteBookFromStudent(studentId, bookId);
        return new ResponseEntity<>("book deleted from student", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable long id){
        this.service.deleteStudent(id);
        return new ResponseEntity<>("student deleted", HttpStatus.OK);
    }

    @PutMapping("/update-student/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable long id, @RequestBody StudentDTO student){
        this.service.updateStudent(id, student);
        return new ResponseEntity<>("student updated", HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-book/{id}")
    public ResponseEntity<String> updateBook(@PathVariable long id, @RequestBody BookDTO b){
        this.service.updateBook(id, b);
        return new ResponseEntity<>("book updated", HttpStatus.OK);
    }

    @PostMapping("/add-course-to-student/{studentId}")
    public ResponseEntity<String> addCourseToStudent(@PathVariable long studentId, @RequestParam String courseName){
        this.service.addCourseToStudent(studentId, courseName);
        return new ResponseEntity<>("course added to student", HttpStatus.OK);
    }

    @DeleteMapping("/delete-course-from-student-by-name/{studentId}")
    public ResponseEntity<String> deleteCourseFromStudentByName(@PathVariable long studentId, @RequestParam String name){
        this.service.deleteCourseFromStudent(studentId, name);
        return new ResponseEntity<>("course deleted from student", HttpStatus.ACCEPTED);
    }
}
