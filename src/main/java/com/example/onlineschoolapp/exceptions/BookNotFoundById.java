package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotFoundById extends RuntimeException{

    public BookNotFoundById(long id){
        super("ERROR: book not found with this id:" + id);
    }
}
