package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoStudentsException extends RuntimeException{

    public NoStudentsException(String message){
        super("ERROR: " +  message);
    }
}
