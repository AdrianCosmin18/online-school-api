package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentNotFoundByEmail extends RuntimeException{

    public StudentNotFoundByEmail(String email){
        super(String.format("ERROR: no student found with this email : %s", email));
    }
}
