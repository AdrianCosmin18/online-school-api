package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotFoundByName extends RuntimeException{

    public BookNotFoundByName(String name){
        super("ERROR: book not found with this title: " + name);
    }
}
