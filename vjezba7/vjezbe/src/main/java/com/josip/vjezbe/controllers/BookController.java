package com.josip.vjezbe.controllers;

import com.josip.vjezbe.entities.Book;
import com.josip.vjezbe.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.josip.vjezbe.exceptions.BookNotFoundException;
import com.josip.vjezbe.exceptions.InvalidRequestException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer publishedYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParams = sort.split(",");
        if (sortParams.length != 2) {
            throw new InvalidRequestException("Sort parameter must contain property and direction (e.g., 'id,asc').");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sortParams[0]).with(Sort.Direction.fromString(sortParams[1]))));

        return bookService.findAllWithPagination(title, author, genre, publishedYear, pageable);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidRequestException("Book title is required");
        }

        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new InvalidRequestException("Book author is required");
        }

        if (book.getGenre() == null || book.getGenre().isEmpty()) {
            throw new InvalidRequestException("Book genre is required");
        }

        if (book.getPublishedYear() <= 0) {
            throw new InvalidRequestException("Book published year is required and must be positive");
        }

        return bookService.save(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookService.findById(id)
                .map(book -> {
                    if (bookDetails.getTitle() == null || bookDetails.getTitle().isEmpty()) {
                        throw new InvalidRequestException("Book title is required for update");
                    }
                    if (bookDetails.getAuthor() == null || bookDetails.getAuthor().isEmpty()) {
                        throw new InvalidRequestException("Book author is required for update");
                    }
                    if (bookDetails.getGenre() == null || bookDetails.getGenre().isEmpty()) {
                        throw new InvalidRequestException("Book genre is required for update");
                    }
                    if (bookDetails.getPublishedYear() <= 0) {
                        throw new InvalidRequestException("Book published year is required for update and must be positive");
                    }
                    book.setTitle(bookDetails.getTitle());
                    book.setPublishedYear(bookDetails.getPublishedYear());
                    book.setGenre(bookDetails.getGenre());
                    book.setAuthor(bookDetails.getAuthor());
                    return ResponseEntity.ok(bookService.save(book));
                })
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isPresent()) {
            bookService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
    }
}