package com.example.onlineschoolapp.exceptions;

import com.example.onlineschoolapp.services.StudentAndBookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentNotFoundById extends RuntimeException{

    public StudentNotFoundById(long id){
        super(String.format("ERROR: no student found with this id: %d", id));
    }
}
