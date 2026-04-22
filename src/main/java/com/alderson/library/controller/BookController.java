package com.alderson.library.controller;

import com.alderson.library.model.Book;
import com.alderson.library.service.BookService;
import com.alderson.library.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int pageNumber,
                        @RequestParam(value = "books_per_page", defaultValue = "5") int pageSize,
                        @RequestParam(value = "sort_by_year", required = false) boolean isSorted) {

        Pageable pageable = isSorted ?
                PageRequest.of(pageNumber, pageSize, Sort.by("year")) :
                PageRequest.of(pageNumber, pageSize);

        model.addAttribute("books", bookService.findAll(pageable));
        return "books/all";
    }

    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        model.addAttribute("people", personService.findAll());
        return "books/book";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping("/new")
    public String saveBook(@ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "books/new";
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResultesult) {
        if (bindingResultesult.hasErrors()) {
            return "books/edit";
        }
        book.setId(id);
        try {
            bookService.updateBook(id, book);
        } catch (DataIntegrityViolationException e) {
            bindingResultesult.rejectValue("title", "error.book", "This title already exists");
            return "books/edit";
        }
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add-owner")
    public String addOwner(@PathVariable("id") Long bookId, @RequestParam("personId") Long personId) {
        bookService.addOwner(bookId, personId);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/delete-owner")
    public String deleteOwner(@PathVariable("id") Long bookId) {
        bookService.deleteOwner(bookId);
        return "redirect:/books/" + bookId;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String findBookPage(Model model) {
        model.addAttribute("searchPerformed", false);
        return "books/search";
    }

    @PostMapping("/search")
    public String findBookByTitle(Model model, @RequestParam("title") String title) {
        Book foundBook = bookService.findBookByTitle(title);
        model.addAttribute("foundBook", foundBook);
        model.addAttribute("searchPerformed", true);
        return "books/search";
    }
}
