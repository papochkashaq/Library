package com.alderson.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.alderson.library.model.Book;
import com.alderson.library.repository.BookRepository;
import com.alderson.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(Long id, Book book) {
        Book updatedBook = bookRepository.findById(id).get();
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setTitle(book.getTitle());
        updatedBook.setYear(book.getYear());
        updatedBook.setYear(book.getYear());
        updatedBook.setOwner(book.getOwner());
        bookRepository.save(updatedBook);
    }

    public Book findBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        } else {
            return null;
        }
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void addOwner(Long bookId, Long personId) {
        Book updatedBook = bookRepository.findById(bookId).get();
        updatedBook.setOwner(personRepository.findById(personId).get());
        updatedBook.setDateOfTaking(LocalDate.now());
        bookRepository.save(updatedBook);
    }

    public void deleteOwner(Long bookId) {
        Book updatedBook = bookRepository.findById(bookId).get();
        updatedBook.setOwner(null);
        updatedBook.setDateOfTaking(null);
        bookRepository.save(updatedBook);
    }

    public Book findBookByTitle(String title) {
        return bookRepository.findFirstByTitleContaining(title).orElse(null);
    }
}
