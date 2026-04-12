package com.crio.rent_read.controller;

import com.crio.rent_read.dto.request.BookRequest;
import com.crio.rent_read.dto.request.BookUpdateRequest;
import com.crio.rent_read.dto.response.BookResponse;
import com.crio.rent_read.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

  private final BookService bookService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
    log.info("Create book request received: {}", bookRequest.getTitle());
    BookResponse response = bookService.createBook(bookRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<BookResponse>> getAllBooks() {
    log.info("Get all books request received");
    List<BookResponse> books = bookService.getAllBooks();
    return ResponseEntity.ok(books);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
    log.info("Get book by id request received: {}", id);
    BookResponse response = bookService.getBookById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BookResponse> updateBook(@PathVariable Long id,
      @Valid @RequestBody BookUpdateRequest bookUpdateRequest) {
    log.info("Update book request received for id: {}", id);
    BookResponse response = bookService.updateBook(id, bookUpdateRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    log.info("Delete book request received for id: {}", id);
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();
  }
}