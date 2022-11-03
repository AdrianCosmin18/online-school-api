package com.example.onlineschoolapp.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private String name;
    private LocalDate createdAt;
}
