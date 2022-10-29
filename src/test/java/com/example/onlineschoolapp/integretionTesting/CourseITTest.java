package com.example.onlineschoolapp.integretionTesting;

import com.example.onlineschoolapp.OnlineSchoolAppApplication;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.repository.CourseRepo;
import com.example.onlineschoolapp.services.CourseService;
import com.example.onlineschoolapp.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = OnlineSchoolAppApplication.class)
//@TestPropertySource(locations = "classpath:application-it.properties")
public class CourseITTest {

    @MockBean
    private CourseRepo courseRepo;

    @InjectMocks
    private CourseService courseService;

    @Autowired
    private MockMvc restMockMVC;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void shouldGetAllCourses() throws Exception{
        List<Course> courses = new ArrayList<>();
        Faker f = new Faker();
        for (int i = 0; i< 10;i++) {
            courses.add(Course.builder().id((long) i).department(f.commerce().department()).name(f.commerce().productName()).build());
        }
        doReturn(courses).when(courseRepo).findAll();
        this.restMockMVC.perform(get("/online-school/api/v1/courses/all-courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(courses)));
    }


}
