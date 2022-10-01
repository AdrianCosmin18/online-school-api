package com.example.onlineschoolapp.services;

import com.example.onlineschoolapp.dto.CourseDTO;
import com.example.onlineschoolapp.exceptions.CourseNameAlreadyExists;
import com.example.onlineschoolapp.exceptions.CourseNotFoundByDepartment;
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

        Course course = new Course();
        course.setId(2L);
        course.setDepartment("Math");
        course.setName("Analiza 2");

        doReturn(Optional.of(course)).when(courseRepo).findById(course.getId());

        assertThat(courseService.getCourseById(course.getId()).getId()).isEqualTo(2L);
    }

    @Test
    public void shouldGetCourseByIdException(){

        Course course = new Course();
        course.setId(2L);
        course.setDepartment("Math");
        course.setName("Analiza 2");
        doReturn(Optional.empty()).when(courseRepo).findById(course.getId());
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

    @Test
    public void shouldThrowExceptionAddCourse(){

        CourseDTO courseDTO = CourseDTO.builder().name("alg fundam").department("info").build();

        doReturn(Optional.of(courseDTO)).when(courseRepo).getCourseByName(courseDTO.getName());
        assertThrows(CourseNameAlreadyExists.class, () ->{
            courseService.addCourse(courseDTO);
        } );
    }

    @Test
    public void shouldDeleteCourseById(){
        Course course = new Course();
        course.setId(2L);
        course.setDepartment("Math");
        course.setName("Analiza 2");

        doReturn(Optional.of(course)).when(courseRepo).findById(course.getId());
        courseService.deleteCourseById(course.getId());
        then(courseRepo).should().deleteById(course.getId());
    }

    @Test
    public void shouldThrowExceptionDeleteCourseById(){
        Course course = new Course();
        course.setId(2L);
        course.setDepartment("Math");
        course.setName("Analiza 2");

        doReturn(Optional.empty()).when(courseRepo).findById(course.getId());
        assertThrows(CourseNotFoundById.class, () -> courseService.deleteCourseById(course.getId()));
    }

    @Test
    public void shouldGetCoursesByDepartment(){

        Course course1 = new Course();
        course1.setId(2L);
        course1.setDepartment("Math");
        course1.setName("Analiza 2");

        Course course2 = new Course();
        course2.setId(3L);
        course2.setDepartment("Math");
        course2.setName("Analiza 1");

        Course course3 = new Course();
        course3.setId(10L);
        course3.setDepartment("Info");
        course3.setName("IA");

        ArrayList<Course> courses = new ArrayList<>(List.of(course1, course2, course3));
        doReturn(Optional.of(courses)).when(courseRepo).getCoursesByDepartment("Math");
        assertThat(courseService.getCoursesByDepartment(course1.getDepartment())).isEqualTo(new ArrayList(List.of(course1, course2, course3)));
    }

    @Test
    void shouldThrowExceptionGetCoursesByDepartment(){

        doReturn(Optional.of(new ArrayList())).when(courseRepo).getCoursesByDepartment("Math");
        assertThrows(CourseNotFoundByDepartment.class, () -> courseService.getCoursesByDepartment("Math"));
    }

    @Test
    void shouldUpdateCourseById(){
        Course course = new Course();
        course.setId(2L);
        course.setDepartment("Math");
        course.setName("Analiza 2");

        CourseDTO courseDTO = new CourseDTO("Algebra 2", "Science");
        doReturn(Optional.of(course)).when(courseRepo).findById(course.getId());
        courseService.updateCourseById(courseDTO, course.getId());
        then(courseRepo).should().updateCourseById(courseDTO.getName(),courseDTO.getDepartment(),course.getId());
    }

    @Test
    void shouldThrowExceptionUpdateCourseById(){
        Course course = new Course();
        course.setId(2L);
        course.setDepartment("Math");
        course.setName("Analiza 2");

        CourseDTO courseDTO = new CourseDTO("Algebra 2", "Science");
        doReturn(Optional.empty()).when(courseRepo).findById(course.getId());
        assertThrows(CourseNotFoundById.class, () -> courseService.updateCourseById(courseDTO, course.getId()));
    }



}