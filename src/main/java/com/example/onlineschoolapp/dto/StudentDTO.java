package com.example.onlineschoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Double age;
}
