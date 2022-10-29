package com.example.onlineschoolapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseNotFoundByDepartment extends RuntimeException {

    public CourseNotFoundByDepartment(String department){
        super("ERROR: There is no course from " + department + " department");
    }
}
