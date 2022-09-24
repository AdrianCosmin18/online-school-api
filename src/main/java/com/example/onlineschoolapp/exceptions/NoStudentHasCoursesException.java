package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoStudentHasCoursesException extends RuntimeException{

    public NoStudentHasCoursesException(){
        super("ERROR: no student hasn't registered to any course yet");
    }
}
