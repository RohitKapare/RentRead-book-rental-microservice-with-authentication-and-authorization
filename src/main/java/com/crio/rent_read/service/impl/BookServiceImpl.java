package com.crio.rent_read.service.impl;

import com.crio.rent_read.dto.request.BookRequest;
import com.crio.rent_read.dto.request.BookUpdateRequest;
import com.crio.rent_read.dto.response.BookResponse;
import com.crio.rent_read.entity.Book;
import com.crio.rent_read.exception.ResourceNotFoundException;
import com.crio.rent_read.mapper.BookMapper;
import com.crio.rent_read.repository.BookRepository;
import com.crio.rent_read.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  @Override
  public BookResponse createBook(BookRequest bookRequest) {
    log.info("Creating new book: {}", bookRequest.getTitle());
    Book book = bookMapper.toBook(bookRequest);
    Book savedBook = bookRepository.save(book);
    log.info("Book created successfully with id: {}", savedBook.getId());
    return bookMapper.toBookResponse(savedBook);
  }

  @Override
  public List<BookResponse> getAllBooks() {
    log.info("Fetching all books");
    return bookRepository.findAll().stream()
        .map(bookMapper::toBookResponse)
        .collect(Collectors.toList());
  }

  @Override
  public BookResponse getBookById(Long id) {
    log.info("Fetching book with id: {}", id);
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    return bookMapper.toBookResponse(book);
  }

  @Override
  public BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
    log.info("Updating book with id: {}", id);
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    bookMapper.updateBookFromDto(bookUpdateRequest, book);
    Book updatedBook = bookRepository.save(book);
    log.info("Book updated successfully with id: {}", updatedBook.getId());
    return bookMapper.toBookResponse(updatedBook);
  }

  @Override
  public void deleteBook(Long id) {
    log.info("Deleting book with id: {}", id);
    if (!bookRepository.existsById(id)) {
      throw new ResourceNotFoundException("Book not found with id: " + id);
    }
    bookRepository.deleteById(id);
    log.info("Book deleted successfully with id: {}", id);
  }
}
