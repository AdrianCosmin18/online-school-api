package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentNotHavingCourseException extends RuntimeException{

    public StudentNotHavingCourseException(){
        super("ERROR: student doesn't have in his list of courses this course");
    }
}
