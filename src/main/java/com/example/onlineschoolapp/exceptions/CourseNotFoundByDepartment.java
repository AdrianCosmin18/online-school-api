package com.example.onlineschoolapp.exceptions;

public class CourseNotFoundByDepartment extends RuntimeException {

    public CourseNotFoundByDepartment(String department){
        super("ERROR: There is nou course from " + department + " department");
    }
}
