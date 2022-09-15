package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseNameAlreadyExists extends RuntimeException{

    public CourseNameAlreadyExists(){
        super("ERROR: already exists a course with this name");
    }
}
