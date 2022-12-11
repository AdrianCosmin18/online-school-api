package com.example.onlineschoolapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "Book")
@Table(name = "book")
public class Book {

    @Id
    @SequenceGenerator(name = "book_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",
                referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "student_id_fk")
    )
    @JsonBackReference//nu mi va mai arata sutdentul
    private Student student;

    @NotNull
    @Column(
            name = "book_name",
            columnDefinition = "varchar(50)"
    )
    private String name;

    @NotNull(message = "Local date is required")
    @Column(name = "created_at", columnDefinition = "varchar(50)")
    //@DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate createdAt;

    public Book(String name, LocalDate createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    @Override
    public String toString(){
        String text = "";
        text += "{id:" + id + "\n";
        text += "name:" + name + "\n";
        text += "crated at:" + createdAt + "}";

        return text;
    }

}
