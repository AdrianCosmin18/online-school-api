package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyHasThisCourseException extends RuntimeException{

    public AlreadyHasThisCourseException(){
        super("ERROR: the student is already enrolled at this courser, you can't enroll him again");
    }
}
