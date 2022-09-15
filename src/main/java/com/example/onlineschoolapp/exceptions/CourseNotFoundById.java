package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseNotFoundById extends RuntimeException{

    public CourseNotFoundById(long id){
        super("ERROR:course not found with this id:" + id);
    }
}
