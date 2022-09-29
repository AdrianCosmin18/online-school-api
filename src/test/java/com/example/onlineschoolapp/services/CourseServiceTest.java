package com.example.onlineschoolapp.services;

import com.example.onlineschoolapp.dto.CourseDTO;
import com.example.onlineschoolapp.exceptions.CourseNotFoundById;
import com.example.onlineschoolapp.exceptions.NoCourseException;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.repository.CourseRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.EnumOptions;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.print.CancelablePrintJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepo courseRepo;


    @InjectMocks
    private  CourseService courseService;

    @Captor
    private ArgumentCaptor<Course> courseArgumentCaptor;


    @BeforeEach
    void setup(){

        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void shouldGetAll(){
        List<Course> courses = new ArrayList<>();
        Faker f = new Faker();
        for (int i = 0; i< 10;i++) {
            courses.add(Course.builder().id((long) i).department(f.commerce().department()).name(f.commerce().productName()).build());
        }
        doReturn(courses).when(courseRepo).findAll();
        assertThat(courseService.getCourses().size()).isEqualTo(10);
    }

    @Test
    public void shouldGetAllException(){

        List<Course> courses = new ArrayList<>();
        doReturn(courses).when(courseRepo).findAll();
        assertThrows(NoCourseException.class, () -> courseService.getCourses());
    }

    @Test
    public void shouldGetCourseById(){

        Course course = Course.builder().id(10L).name("IA").department("Informatica").build();
        doReturn(Optional.of(course)).when(courseRepo).findById(course.getId());
        assertThat(courseService.getCourseById(course.getId())).isEqualTo(Course.builder().id(10L).name("IA").department("Informatica").build());
        assertThat(courseService.getCourseById(course.getId()).getId()).isEqualTo(10L);
    }

    @Test
    public void shouldGetCourseByIdException(){

        doReturn(Optional.of(Course.builder().id(1L).department("Math").name("Analiza 2").build())).when(courseRepo).findById(1L);
        assertThrows(CourseNotFoundById.class, () -> courseService.getCourseById(2L));
    }

    @Test
    public void shouldAddCourse(){
        CourseDTO courseDTO = CourseDTO.builder().name("alg fundam").department("info").build();

        doReturn(Optional.empty()).when(courseRepo).getCourseByName(courseDTO.getName());

        courseService.addCourse(courseDTO);

        then(courseRepo).should().save(courseArgumentCaptor.capture());

        assertThat(courseArgumentCaptor.getValue()).isEqualTo(new Course(courseDTO.getName(),courseDTO.getDepartment()));
    }

}