package com.example.onlineschoolapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Student")
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(name = "email_unique", columnNames = "email")})
public class Student {

    @Id
    @SequenceGenerator(name = "student_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty
    @Email
    @Column(name = "email",columnDefinition = "varchar(50)")
    private String email;

    @Column(name = "age", nullable = false)
    private Integer age;


    public Student(String firstName, String lastName, String email, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }
}
