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
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private BookRepo bookRepo;



    @InjectMocks
    private StudentAndBookService service;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllStudents(){

        List<Student> students = new ArrayList<>();
        Faker f = new Faker();
        for (int i = 0; i< 10;i++) {
            students.add(Student.builder().id((long) i).age((double) (20 + i)).email(f.name().fullName() + "@yahoo.com").firstName(f.name().firstName()).lastName(f.name().lastName()).build());
        }

        doReturn(students).when(studentRepo).findAll();
        assertThat(service.getAllStudents().size()).isEqualTo(students.size());
    }

    @Test
    void shouldThrowExceptionGetAllStudents(){

        doReturn(new ArrayList<>()).when(studentRepo).findAll();
        assertThrows(NoStudentsException.class, () -> service.getAllStudents());
    }

    @Test
    void shouldGetStudentById(){
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);

        doReturn(Optional.of(student)).when(studentRepo).findById(student.getId());
        assertThat(service.getStudentById(student.getId())).isEqualTo(student);
    }

    @Test
    void shouldThrowExceptionGetStudentById(){
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);

        doReturn(Optional.empty()).when(studentRepo).findById(student.getId());
        assertThrows(StudentNotFoundById.class, () -> service.getStudentById(student.getId()));
    }

    @Test
    void shouldGetAllBooks(){

        List<Book> books = new ArrayList<>();
        Faker f = new Faker();
        for (int i = 0; i< 10;i++){
            books.add(new Book(f.book().title(), LocalDate.parse("2021-10-25").plusDays(i)));
        }
        doReturn(books).when(bookRepo).findAll();
        assertThat(service.getAllBooks().get(0)).isEqualTo(books.get(0));
    }

    @Test
    void shouldThrowExceptionGetAllBooks(){

        doReturn(new ArrayList<>()).when(bookRepo).findAll();
        assertThrows(NoBooksFoundException.class, () -> service.getAllBooks());
    }

    @Test
    void shouldGetBookById(){

        Book book = new Book();
        book.setId(1l);
        book.setName("LOTR 2");
        book.setCreatedAt(LocalDate.parse("07-12-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        doReturn(Optional.of(book)).when(bookRepo).findById(book.getId());
        assertThat(service.getBookById(book.getId())).isEqualTo(book);
    }

    @Test
    void shouldThrowExceptionGetBookById(){

        Book book = new Book();
        book.setId(1l);
        book.setName("LOTR 2");
        book.setCreatedAt(LocalDate.parse("07-12-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        doReturn(Optional.empty()).when(bookRepo).findById(1l);
        assertThrows(BookNotFoundById.class, () -> service.getBookById(book.getId()));
    }

    @Test
    void shouldAddStudent(){
        StudentDTO student = new StudentDTO();
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");


        doReturn(Optional.empty()).when(studentRepo).getStudentByEmail(student.getEmail());
        service.addStudent(student);
        then(studentRepo).should().save(studentArgumentCaptor.capture());
        assertThat(studentArgumentCaptor.getValue()).isEqualTo(new Student(student.getFirstName(), student.getLastName(), student.getEmail(), student.getAge()));
    }

    @Test
    void shouldThrowExceptionAddStudent(){
        Student student = new Student();
        student.setEmail("cosmin_ndlc@yahoo.com");

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("Cosmin");
        studentDTO.setLastName("Nedelcu");
        studentDTO.setAge(21d);
        studentDTO.setEmail("cosmin_ndlc@yahoo.com");

        doReturn(Optional.of(student)).when(studentRepo).getStudentByEmail(student.getEmail());
        assertThrows(StudentEmailAlreadyExistsException.class, () -> service.addStudent(studentDTO));
    }


    @Test
    public void shouldAddBookToStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        BookDTO book = new BookDTO();
        book.setName("LOTR1");
        book.setCreatedAt(LocalDate.parse("2020-09-20"));

        doReturn(Optional.of(student)).when(studentRepo).findById(student.getId());
        doReturn(Optional.empty()).when(bookRepo).getBookByName(book.getName());
        service.addBookToStudent(student.getId(), book);

        then(studentRepo).should().save(studentArgumentCaptor.capture());
        assertThat(studentArgumentCaptor.getValue()).isEqualTo(new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getAge()));
    }

    @Test
    public void shouldThrowException1AddBookToStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        BookDTO book = new BookDTO();
        book.setName("LOTR1");
        book.setCreatedAt(LocalDate.parse("2020-09-20"));

        doReturn(Optional.empty()).when(studentRepo).findById(student.getId());
        assertThrows(StudentNotFoundById.class, () -> service.addBookToStudent(1l, book));
    }

    @Test
    public void shouldThrowException2AddBookToStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        BookDTO book = new BookDTO();
        book.setName("LOTR1");
        book.setCreatedAt(LocalDate.parse("2020-09-20"));

        doReturn(Optional.of(student)).when(studentRepo).findById(student.getId());
        doReturn(Optional.of(book)).when(bookRepo).getBookByName(book.getName());
        assertThrows(BookNameAlreadyExistsException.class, () -> service.addBookToStudent(1l, book));
    }

    @Test
    public void shouldDeleteBookFromStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        Book book = new Book();
        book.setId(10L);
        book.setName("LOTR1");
        book.setCreatedAt(LocalDate.parse("2020-09-20"));

        doReturn(Optional.of(student)).when(studentRepo).findById(student.getId());
        doReturn(Optional.of(book)).when(bookRepo).findById(book.getId());
        service.deleteBookFromStudent(student.getId(), book.getId());
        then(studentRepo).should().save(studentArgumentCaptor.capture());
        assertThat(studentArgumentCaptor.getValue()).isEqualTo(new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getAge()));
    }

    @Test
    void shouldThrowException1DeleteBookFromStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        Book book = new Book();
        book.setId(1l);
        book.setName("LOTR1");
        book.setCreatedAt(LocalDate.parse("2020-09-20"));

        doReturn(Optional.of(book)).when(bookRepo).findById(1l);
        doReturn(Optional.empty()).when(studentRepo).findById(student.getId());
        assertThrows(StudentNotFoundById.class, () -> service.deleteBookFromStudent(student.getId(), book.getId()));
    }

    @Test
    void shouldThrowException2DeleteBookFromStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        Book book = new Book();
        book.setId(1l);
        book.setName("LOTR1");
        book.setCreatedAt(LocalDate.parse("2020-09-20"));

        doReturn(Optional.empty()).when(bookRepo).findById(1l);
        assertThrows(BookNotFoundById.class, () -> service.deleteBookFromStudent(1l, book.getId()));
    }

    @Test
    void shouldDeleteStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        doReturn(Optional.of(student)).when(studentRepo).findById(student.getId());
        service.deleteStudent(student.getId());
        then(studentRepo).should().deleteById(1l);
    }

    @Test
    void shouldThrowExceptionDeleteStudent(){

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        doReturn(Optional.empty()).when(studentRepo).findById(student.getId());
        assertThrows(StudentNotFoundById.class, () -> service.deleteStudent(1l));
    }

    @Test
    void shouldUpdateStudent(){
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Cosmin");
        student.setLastName("Nedelcu");
        student.setAge(21d);
        student.setEmail("cosmin_ndlc@yahoo.com");

        StudentDTO studentDTO = new StudentDTO("Adrian Cosmin", "Nedelcu", "cosmin_ndlc@yahoo.com", 22d);
        doReturn(Optional.of(student)).when(studentRepo).findById(student.getId());
        doReturn(Optional.empty()).when(studentRepo).getStudentByEmail(student.getEmail());
        service.updateStudent(student.getId(), studentDTO);
        then(studentRepo).should().updateById(1L, studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), studentDTO.getAge());
    }


    @Test
    void shouldGetMostPopularCourse(){

        Faker f = new Faker();

        List<Student> students = new ArrayList<>();
        for (int i = 0 ; i < 3; i++){
            students.add(new Student((long) i, f.name().firstName(), f.name().lastName(), f.name() + "@yahoo.com", (double) f.number().numberBetween(20,50)));
        }

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().id(0L).name("IA").department("info").build());
        courses.add(Course.builder().id(1L).name("IS").department("info").build());
        courses.add(Course.builder().id(2L).name("SGBD").department("info").build());
        courses.add(Course.builder().id(3L).name("Geometry").department("math").build());

        students.get(0).addCourse(courses.get(0));//IA
        students.get(1).addCourse(courses.get(1));//IS
        students.get(2).addCourse(courses.get(2));//SGBD
        students.get(0).addCourse(courses.get(3));//Geo
        students.get(1).addCourse(courses.get(1));//IS
        students.get(2).addCourse(courses.get(2));//SGBD
        students.get(1).addCourse(courses.get(3));//Geo
        students.get(2).addCourse(courses.get(1));//IS

        doReturn(students).when(studentRepo).findAll();
        assertThat(service.mostPopularCourse().getName()).isEqualTo("IS");
    }

    @Test
    void shouldThrowExceptionMostPopularCourse(){

        List<Student> students = new ArrayList<>();
        doReturn(students).when(studentRepo).findAll();
        assertThrows(NoStudentsException.class, () -> service.getAllStudents());
    }

    @Test
    void shouldReturnMostUnpopularCourse(){

        Faker f = new Faker();

        List<Student> students = new ArrayList<>();
        for (int i = 0 ; i < 3; i++){
            students.add(new Student((long) i, f.name().firstName(), f.name().lastName(), f.name() + "@yahoo.com", (double) f.number().numberBetween(20,50)));
        }

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().id(0L).name("IA").department("info").build());
        courses.add(Course.builder().id(1L).name("IS").department("info").build());
        courses.add(Course.builder().id(2L).name("SGBD").department("info").build());
        courses.add(Course.builder().id(3L).name("Geometry").department("math").build());

        students.get(0).addCourse(courses.get(0));//IA
        students.get(1).addCourse(courses.get(1));//IS
        students.get(2).addCourse(courses.get(2));//SGBD
        students.get(0).addCourse(courses.get(3));//Geo
        students.get(1).addCourse(courses.get(1));//IS
        students.get(2).addCourse(courses.get(2));//SGBD
        students.get(1).addCourse(courses.get(3));//Geo
        students.get(2).addCourse(courses.get(1));//IS

        doReturn(students).when(studentRepo).findAll();
        assertThat(service.mostUnpopularCourse()).isEqualTo(Course.builder().id(0L).name("IA").department("info").build());
    }

    @Test
    void shouldThrowExceptionMostUnpopularCourse(){

        List<Student> students = new ArrayList<>();
        doReturn(students).when(studentRepo).findAll();
        assertThrows(NoStudentsException.class, () -> service.getAllStudents());
    }

}













