package com.crio.rent_read.service;

import com.crio.rent_read.dto.request.BookRequest;
import com.crio.rent_read.dto.request.BookUpdateRequest;
import com.crio.rent_read.dto.response.BookResponse;

import java.util.List;

public interface BookService {
  BookResponse createBook(BookRequest bookRequest);
  List<BookResponse> getAllBooks();
  BookResponse getBookById(Long id);
  BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest);
  void deleteBook(Long id);
}