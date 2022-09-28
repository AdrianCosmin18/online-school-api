package com.example.onlineschoolapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference//arata lista de carti
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Book> books = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "enrolled_courses",
                joinColumns = {@JoinColumn(name = "student_id")},
                inverseJoinColumns = {@JoinColumn(name = "course_id")})
    @JsonManagedReference
    private List<Course> courses = new ArrayList<>();



    public Student(String firstName, String lastName, String email, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    public void addBook(Book book){
        this.books.add(book);
        book.setStudent(this);
    }

    public void removeBook(Book book){

        this.books.remove(book);
    }

    public void addCourse(Course c){
        this.courses.add(c);
    }

    public void removeCourse(Course c){
        this.courses.remove(c);
    }

    @Override
    public String toString(){
        String text = "";
        text += "{id:" + id + "\n";
        text += "firstName:" + firstName + "\n";
        text += "lastName:" + lastName + "\n";
        text += "email:" + email + "\n";
        text += "age:" + age + "}";

        return text;
    }
}
