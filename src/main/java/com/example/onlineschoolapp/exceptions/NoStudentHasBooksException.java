package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoStudentHasBooksException extends RuntimeException{

    public NoStudentHasBooksException(){
        super("ERROR: not student has rent any book yet !!!");
    }
}
