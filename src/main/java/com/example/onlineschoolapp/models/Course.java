package com.example.onlineschoolapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Course")
@Table(name = "course")
public class Course {
    @Id
    @SequenceGenerator(name = "course_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    private Long id;



    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "department", columnDefinition = "varchar(50)")
    private String department;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)//
    @JsonBackReference
    private List<Student> students = new ArrayList<>();

    public Course(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public void addStudent(Student s){
        this.students.add(s);
    }


    @Override
    public String toString(){
        String text = "";
        text += "{id:" + id + "\n";
        text += "name:" + name + "\n";
        text += "department:" + department + "}";
        return text;
    }

    @Override
    public boolean equals(Object o){
        Course course = (Course) o;
        return course.getName().equals(this.getName()) && course.getDepartment().equals(this.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDepartment());
    }
}
