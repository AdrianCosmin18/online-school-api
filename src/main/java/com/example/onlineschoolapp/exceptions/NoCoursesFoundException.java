package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoCoursesFoundException extends RuntimeException{

    public NoCoursesFoundException(){
        super("ERROR: Does not exist any course in database");
    }
}
