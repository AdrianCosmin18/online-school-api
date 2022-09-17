package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNameAlreadyExistsException extends RuntimeException{

    public BookNameAlreadyExistsException(){
        super("ERROR: already exists a book with this name registered to a student");
    }
}
