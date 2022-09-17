package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentEmailAlreadyExistsException extends RuntimeException {

    public StudentEmailAlreadyExistsException(String mail){
        super("ERROR: already exists an student with this email: " + mail);
    }
}
