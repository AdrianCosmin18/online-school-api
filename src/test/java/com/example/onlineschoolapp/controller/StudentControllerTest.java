package com.example.onlineschoolapp.controller;

import com.example.onlineschoolapp.dto.BookDTO;
import com.example.onlineschoolapp.dto.CourseDTO;
import com.example.onlineschoolapp.dto.StudentDTO;
import com.example.onlineschoolapp.exceptions.*;
import com.example.onlineschoolapp.models.Book;
import com.example.onlineschoolapp.models.Student;
import com.example.onlineschoolapp.services.StudentAndBookService;
import com.example.onlineschoolapp.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentAndBookService service;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private StudentController controller;
    private ObjectMapper mapper =  new ObjectMapper();

    @BeforeEach
    void setup(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Test
    void shouldGetStudents() throws Exception{
        List<Student> students = new ArrayList<>();
        students.add(Student.builder().firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build());
        students.add(Student.builder().firstName("Andrei").lastName("Radu").age(21D).email("asd@yahoo.com").build());

        doReturn(students).when(service).getAllStudents();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/all-students")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(students)));
    }

    @Test
    void shouldThrowExceptionGetStudents() throws Exception{
        doThrow(NoStudentsException.class).when(service).getAllStudents();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/all-students")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetStudentById() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        doReturn(student).when(service).getStudentById(student.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/get-student-by-id/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(student)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionGetStudedntById() throws Exception{
        doThrow(StudentNotFoundById.class).when(service).getStudentById(1L);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/get-student-by-id/1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBooks() throws Exception{
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().name("GOT").createdAt(LocalDate.parse("2022-12-12")).build());
        books.add(Book.builder().name("LOTR").createdAt(LocalDate.parse("2021-10-09")).build());
        doReturn(books).when(service).getAllBooks();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/all-books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(books)));
    }

    @Test
    void shouldThrowExceptionGetBooks() throws Exception{
        doThrow(NoBooksFoundException.class).when(service).getAllBooks();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/all-books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBookById() throws Exception{
        Book b = Book.builder().name("GOT").id(10L).build();
        doReturn(b).when(service).getBookById(b.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/get-book-by-id/" + b.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionGetBookById() throws Exception{
        doThrow(BookNotFoundById.class).when(service).getBookById(1L);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/online-school/api/v1/students/get-book-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddStudent() throws Exception{
        StudentDTO studentDTO = StudentDTO.builder().firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/online-school/api/v1/students/add-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowExceptionAddStudent() throws Exception{
        StudentDTO studentDTO = StudentDTO.builder().firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        doThrow(StudentEmailAlreadyExistsException.class).when(service).addStudent(studentDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/online-school/api/v1/students/add-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddBookToStudent() throws Exception{
        BookDTO b = BookDTO.builder().name("GOT").build();
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/online-school/api/v1/students/add-book-to-student/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldThrowException1AddBookToStudent() throws Exception{
        BookDTO b = BookDTO.builder().name("GOT").build();
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        doThrow(StudentNotFoundById.class).when(service).addBookToStudent(student.getId(), b);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/online-school/api/v1/students/add-book-to-student/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowException2AddBookToStudent() throws Exception{

        BookDTO b = BookDTO.builder().name("GOT").build();
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        doThrow(BookNameAlreadyExistsException.class).when(service).addBookToStudent(student.getId(), b);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/online-school/api/v1/students/add-book-to-student/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldDeleteBookFromStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        Book b = Book.builder().name("GOT").id(10L).build();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/students/delete-book-from-student/2/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldThrowException1DeleteBookFromStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        Book b = Book.builder().name("GOT").id(10L).build();
        doThrow(BookNotFoundById.class).when(service).deleteBookFromStudent(student.getId(), b.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/students/delete-book-from-student/2/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowException2DeleteBookFromStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        Book b = Book.builder().name("GOT").id(10L).build();
        doThrow(StudentNotFoundById.class).when(service).deleteBookFromStudent(student.getId(), b.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/students/delete-book-from-student/2/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(b)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/students/delete-student/"+student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(student)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionDeleteStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        doThrow(StudentNotFoundById.class).when(service).deleteStudent(student.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/students/delete-student/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(student)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        StudentDTO studentDTO = StudentDTO.builder().firstName("Adrian Cosmin").lastName("Nedelcu").age(23d).email("cosmin_ndlc@yahoo.com").build();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/online-school/api/v1/students/update-student/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldThrowException1UpdateStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        StudentDTO studentDTO = StudentDTO.builder().firstName("Adrian Cosmin").lastName("Nedelcu").age(23d).email("cosmin_ndlc@yahoo.com").build();
        doThrow(StudentNotFoundById.class).when(service).updateStudent(student.getId(), studentDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/online-school/api/v1/students/update-student/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowException2UpdateStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        StudentDTO studentDTO = StudentDTO.builder().firstName("Adrian Cosmin").lastName("Nedelcu").age(23d).email("cosmin_ndlc@yahoo.com").build();
        doThrow(StudentEmailAlreadyExistsException.class).when(service).updateStudent(student.getId(), studentDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/online-school/api/v1/students/update-student/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateBook() throws Exception{
        Book b = Book.builder().name("GOT").id(10L).build();
        BookDTO bookDTO = BookDTO.builder().name("LOTR").build();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/online-school/api/v1/students/update-book/" + b.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
                .andExpect(status().isOk());
    }

    @Test//eroare trebuie modificat
    void shouldAddCourseToStudent() throws Exception{
        Student student = Student.builder().id(2L).firstName("Cosmin").lastName("Nedelcu").age(22D).email("cosmin1304@gmail.com").build();
        CourseDTO courseDTO1 = CourseDTO.builder().name("Advanced Geometry 2").department("Math-Science").build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/online-school/api/v1/students/add-course-to-student/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(courseDTO1.getName())))
                .andExpect(status().isOk());
    }


}
