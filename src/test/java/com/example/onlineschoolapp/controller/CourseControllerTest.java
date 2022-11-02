package com.example.onlineschoolapp.controller;

import com.example.onlineschoolapp.dto.CourseDTO;
import com.example.onlineschoolapp.exceptions.CourseNameAlreadyExists;
import com.example.onlineschoolapp.exceptions.CourseNotFoundByDepartment;
import com.example.onlineschoolapp.exceptions.CourseNotFoundById;
import com.example.onlineschoolapp.exceptions.NoCourseException;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.services.CourseService;
import com.example.onlineschoolapp.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {


    @Mock
    private CourseService courseService;

    @Autowired
    private MockMvc restMockMVC;

    @InjectMocks
    private CourseController courseController;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup(){

        restMockMVC = MockMvcBuilders.standaloneSetup(courseController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();
    }


    @Test
    void testGetAllCourses() throws Exception{
        ModelMapper modelMapper = new ModelMapper();
        List<Course> courses = new ArrayList<>();
        CourseDTO courseDTO1 = CourseDTO.builder().name("Advanced Geometry").department("Math").build();
        CourseDTO courseDTO2 = CourseDTO.builder().name("Security Systems").department("CTI").build();
        courses.add(modelMapper.map(courseDTO1, Course.class));
        courses.add(modelMapper.map(courseDTO2, Course.class));

        doReturn(courses).when(this.courseService).getCourses();
        this.restMockMVC.perform(MockMvcRequestBuilders.get("/online-school/api/v1/courses/all-courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(courses)));
    }

    @Test
    void testGetAllCoursesException() throws Exception{
        List<Course> courses = new ArrayList<>();

        doThrow(NoCourseException.class).when(courseService).getCourses();
        this.restMockMVC.perform(MockMvcRequestBuilders.get("/online-school/api/v1/courses/all-courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCourseById() throws Exception{

        Course course = Course.builder().id(1L).department("Math").name("Calculations").build();

        doReturn(course).when(courseService).getCourseById(course.getId());
        this.restMockMVC.perform(MockMvcRequestBuilders.get("/online-school/api/v1/courses/course-by-id/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(course)));
    }

    @Test
    void testGetCourseByIdException() throws Exception{

        Course course = Course.builder().id(1L).department("Math").name("Calculations").build();

        doThrow(CourseNotFoundById.class).when(courseService).getCourseById(1L);
        ResultActions res= this.restMockMVC.perform(MockMvcRequestBuilders.get("/online-school/api/v1/courses/course-by-id/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCourse() throws Exception{
        CourseDTO courseDTO = CourseDTO.builder().department("Math").name("Calculations").build();

        this.restMockMVC.perform(MockMvcRequestBuilders.post("/online-school/api/v1/courses/add-course")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddCourseException() throws Exception{

        CourseDTO courseDTO = CourseDTO.builder().department("Math").name("Calculations").build();

        doThrow(CourseNameAlreadyExists.class).when(courseService).addCourse(courseDTO);
        this.restMockMVC.perform(MockMvcRequestBuilders.post("/online-school/api/v1/courses/add-course")
                .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCourseById() throws Exception{

        Course course = Course.builder().id(1L).department("Math").name("Calculations").build();

        this.restMockMVC.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/courses/delete-by-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCourseByIdException() throws Exception{
        Course course = Course.builder().id(1L).department("Math").name("Calculations").build();

        doThrow(CourseNotFoundById.class).when(courseService).deleteCourseById(1L);
        this.restMockMVC.perform(MockMvcRequestBuilders.delete("/online-school/api/v1/courses/delete-by-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCoursesByDepartment() throws Exception{

        Course course = Course.builder().id(1l).name("Advanced Geometry").department("Math").build();
        Course course2 = Course.builder().id(2l).name("Security Systems").department("Math").build();
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(course2);

        doReturn(courses).when(courseService).getCoursesByDepartment("Math");
        this.restMockMVC.perform(MockMvcRequestBuilders.get("/online-school/api/v1/courses/get-courses-by-department?department=Math")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(courses)));
    }

    @Test
    void testGetCoursesByDepartmentException() throws Exception{

        doThrow(CourseNotFoundByDepartment.class).when(courseService).getCoursesByDepartment("Math");
        this.restMockMVC.perform(MockMvcRequestBuilders.get("/online-school/api/v1/courses/get-courses-by-department?department=Math")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCourseById() throws Exception{
        Course course = Course.builder().id(1l).name("Advanced Geometry").department("Math").build();
        CourseDTO courseDTO1 = CourseDTO.builder().name("Advanced Geometry 2").department("Math-Science").build();

        this.restMockMVC.perform(MockMvcRequestBuilders.put("/online-school/api/v1/courses/update-course-by-id/1")
                .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseDTO1)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCourseByIdException() throws Exception{

        CourseDTO courseDTO1 = CourseDTO.builder().name("Advanced Geometry 2").department("Math-Science").build();
        doThrow(CourseNotFoundById.class).when(courseService).updateCourseById(courseDTO1, 1);
        this.restMockMVC.perform(MockMvcRequestBuilders.put("/online-school/api/v1/courses/update-course-by-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(courseDTO1)))
                .andExpect(status().isBadRequest());
    }




}
















