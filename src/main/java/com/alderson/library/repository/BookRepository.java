package com.alderson.library.repository;

import java.util.List;
import java.util.Optional;

import com.alderson.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findFirstByTitleContaining(String title);
}
