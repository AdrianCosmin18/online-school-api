package com.example.onlineschoolapp.services;

import com.example.onlineschoolapp.dto.BookDTO;
import com.example.onlineschoolapp.dto.StudentDTO;
import com.example.onlineschoolapp.exceptions.*;
import com.example.onlineschoolapp.models.Book;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.models.Student;
import com.example.onlineschoolapp.repository.BookRepo;
import com.example.onlineschoolapp.repository.CourseRepo;
import com.example.onlineschoolapp.repository.StudentRepo;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class StudentAndBookService {

    private StudentRepo studentRepo;
    private BookRepo bookRepo;
    private CourseRepo courseRepo;

    public StudentAndBookService(StudentRepo studentRepo, BookRepo bookRepo, CourseRepo courseRepo) {
        this.studentRepo = studentRepo;
        this.bookRepo = bookRepo;
        this.courseRepo = courseRepo;
    }

    public List<Student> getAllStudents(){

        List<Student>  students = studentRepo.findAll();
        if(students.size() == 0){
            throw new NoStudentsException("Student list empty");
        }
        else{
            return students;
        }
    }

    public Student getStudentById(long id){

        Optional<Student> student = studentRepo.findById(id);
        if(student.equals(Optional.empty())){
            throw new StudentNotFoundById(id);
        }
        else{
            return student.get();
        }
    }

    public List<Book> getAllBooks(){
        List<Book> books = bookRepo.findAll();
        if (books.size() == 0){
            throw new NoBooksFoundException("Books list empty");
        }
        else{
            return books;
        }
    }

    public Book getBookById(long id){
        Optional<Book> book = bookRepo.findById(id);
        if (book.equals(Optional.empty()))
            throw new BookNotFoundById(id);
        return book.get();
    }

    public void addStudent(StudentDTO s){
        Optional<Student> existingStudent = studentRepo.getStudentByEmail(s.getEmail());
        if (!existingStudent.equals(Optional.empty()))
            throw new StudentEmailAlreadyExistsException(s.getEmail());
        studentRepo.save(new Student(s.getFirstName(), s.getLastName(), s.getEmail(), s.getAge()));
    }

    public void addBookToStudent(long id, BookDTO book){
        Optional<Student> student = studentRepo.findById(id);
        if (student.equals(Optional.empty())){
            throw new StudentNotFoundById(id);
        }
        else {
            Optional<Book> existingBook = bookRepo.getBookByName(book.getName());
            if (!existingBook.equals(Optional.empty())){
                throw new BookNameAlreadyExistsException();
            }
            student.get().addBook(new Book(book.getName(), book.getCreatedAt()));
            studentRepo.save(student.get());
        }
    }

    public void deleteBookFromStudent(long studentId, long bookId){
        Optional<Book> existingBook = bookRepo.findById(bookId);
        if (existingBook.equals(Optional.empty())){
            throw new BookNotFoundById(bookId);
        }
        Optional<Student> existingStudent = studentRepo.findById(studentId);
        if (existingStudent.equals(Optional.empty())){
            throw new StudentNotFoundById(studentId);
        }
        existingStudent.get().removeBook(existingBook.get());
        studentRepo.save(existingStudent.get());
    }

    public void deleteStudent(long id){
        Optional<Student> student = studentRepo.findById(id);
        if (student.equals(Optional.empty())){
            throw new StudentNotFoundById(id);
        }
        studentRepo.deleteById(id);
    }

    public void updateStudent(long id, StudentDTO student){
        studentRepo.findById(id).map(s -> {
            s.setFirstName(student.getFirstName());
            s.setLastName(student.getLastName());
            Optional<Student> existingStudent = studentRepo.getStudentByEmail(student.getEmail());
            if (!existingStudent.equals(Optional.empty()))
                throw new StudentEmailAlreadyExistsException(student.getEmail());
            s.setEmail(student.getEmail());
            s.setAge(student.getAge());
            return studentRepo.save(s);
        }).orElseThrow(()->new StudentNotFoundById(id));
    }

    public void updateBook(long id, BookDTO book){
        bookRepo.findById(id).map(b -> {
            Optional<Book> existingBook = bookRepo.getBookByName(book.getName());
            if (!existingBook.equals(Optional.empty())){
                throw new BookNameAlreadyExistsException();
            }
            b.setName(book.getName());
            b.setCreatedAt(book.getCreatedAt());
            return bookRepo.save(b);
        }).orElseThrow(()-> new BookNotFoundById(id));
    }

    public void addCourseToStudent(long id, String courseName){
        Optional<Course> course = courseRepo.getCourseByName(courseName);
        if (course.equals(Optional.empty())){
            throw new CourseNotFoundByName(courseName);
        }
        Optional<Student> student = studentRepo.findById(id);
        if (student.equals(Optional.empty())){
            throw new StudentNotFoundById(id);
        }
        student.get().addCourse(course.get());
        studentRepo.save(student.get());
    }
}
