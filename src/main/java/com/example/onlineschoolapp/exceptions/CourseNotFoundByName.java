package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseNotFoundByName extends RuntimeException{

    public CourseNotFoundByName(String name){
        super("ERROR: course not found with this name:" + name);
    }
}
