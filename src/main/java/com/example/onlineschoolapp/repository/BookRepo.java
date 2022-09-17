package com.example.onlineschoolapp.repository;

import com.example.onlineschoolapp.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    Optional<Book> getBookByName(String name);
}
