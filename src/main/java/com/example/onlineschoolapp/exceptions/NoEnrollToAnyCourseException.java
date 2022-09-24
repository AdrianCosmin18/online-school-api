package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoEnrollToAnyCourseException extends RuntimeException{

    public NoEnrollToAnyCourseException(){
        super("ERROR: there is no student to attend to any course !!!");
    }
}
