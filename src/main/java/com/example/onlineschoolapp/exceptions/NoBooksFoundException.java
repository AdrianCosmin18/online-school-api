package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoBooksFoundException extends RuntimeException{

    public NoBooksFoundException(String mess){
        super(String.format("ERROR: '%s'", mess));
    }
}
