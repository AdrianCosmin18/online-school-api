package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentNotRentBookEception extends RuntimeException{

    public StudentNotRentBookEception(){
        super("ERROR: this student hasn't rent yet any book");
    }
}
