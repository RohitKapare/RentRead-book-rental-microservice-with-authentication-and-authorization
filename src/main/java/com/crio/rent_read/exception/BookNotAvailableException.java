package com.crio.rent_read.exception;

public class BookNotAvailableException extends RuntimeException {
  public BookNotAvailableException(String message) {
    super(message);
  }
}
