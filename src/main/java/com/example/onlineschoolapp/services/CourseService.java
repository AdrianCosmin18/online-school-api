package com.example.onlineschoolapp.services;

import com.example.onlineschoolapp.dto.CourseDTO;
import com.example.onlineschoolapp.exceptions.CourseNameAlreadyExists;
import com.example.onlineschoolapp.exceptions.CourseNotFoundByDepartment;
import com.example.onlineschoolapp.exceptions.CourseNotFoundById;
import com.example.onlineschoolapp.exceptions.NoCourseException;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.repository.CourseRepo;
import com.example.onlineschoolapp.repository.StudentRepo;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private CourseRepo courseRepo;

    public CourseService(CourseRepo courseRepo) {//injectare prin constructor
        this.courseRepo = courseRepo;
    }


    public List<Course> getCourses(){

        List<Course>courses=courseRepo.findAll();

        if(courses.isEmpty()){
            throw new NoCourseException("Course list empty");
        }
        return  courses;
    }

    public Course getCourseById(long id){
        Optional<Course> course = courseRepo.findById(id);
        if (course.isPresent())
            return course.get();
        throw new CourseNotFoundById(id);
    }

    public void addCourse(CourseDTO c){

        Optional<Course> existingCourse = courseRepo.getCourseByName(c.getName());
        if(!existingCourse.equals(Optional.empty())){
            throw new CourseNameAlreadyExists();
        }
        else{
            courseRepo.save(new Course(c.getName(), c.getDepartment()));
        }
    }

    public void deleteCourseById(long id){
        Optional<Course> course = courseRepo.findById(id);
        if (!course.isPresent()){
            throw new CourseNotFoundById(id);
        }
        else{
            courseRepo.deleteById(id);
        }
    }

    public List<Course> getCoursesByDepartment(String department){
        Optional<List<Course>> courses = courseRepo.getCoursesByDepartment(department);
        if (courses.get().size() == 0){
            throw new CourseNotFoundByDepartment(department);
        }
        else{
            return courses.get();
        }
    }

    public void updateCourseById(CourseDTO c, long id){
        Optional<Course> existingCourse = courseRepo.findById(id);
        if (!existingCourse.isPresent()){
            throw new CourseNotFoundById(id);
        }
        else{
            courseRepo.updateCourseById(c.getName(), c.getDepartment(), id);
        }
    }
}






















