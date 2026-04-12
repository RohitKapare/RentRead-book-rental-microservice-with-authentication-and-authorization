package com.crio.rent_read.exception;

import com.crio.rent_read.dto.response.ErrorResponse;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.error("Resource not found: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message(ex.getMessage())
        .httpStatus(HttpStatus.NOT_FOUND)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
    log.error("Duplicate resource: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message(ex.getMessage())
        .httpStatus(HttpStatus.CONFLICT)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RentalLimitExceededException.class)
  public ResponseEntity<ErrorResponse> handleRentalLimitExceeded(RentalLimitExceededException ex) {
    log.error("Rental limit exceeded: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message(ex.getMessage())
        .httpStatus(HttpStatus.BAD_REQUEST)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BookNotAvailableException.class)
  public ResponseEntity<ErrorResponse> handleBookNotAvailable(BookNotAvailableException ex) {
    log.error("Book not available: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message(ex.getMessage())
        .httpStatus(HttpStatus.BAD_REQUEST)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleSpringAccessDenied(
      org.springframework.security.access.AccessDeniedException ex) {
    log.error("Access denied: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message("Access Denied: You don't have permission to perform this action")
        .httpStatus(HttpStatus.FORBIDDEN)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
    log.error("Bad credentials: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message("Invalid email or password")
        .httpStatus(HttpStatus.UNAUTHORIZED)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    String messages = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    log.error("Validation failed: {}", messages);
    ErrorResponse error = ErrorResponse.builder()
        .message(messages)
        .httpStatus(HttpStatus.BAD_REQUEST)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("Unexpected error: ", ex);
    ErrorResponse error = ErrorResponse.builder()
        .message("An unexpected error occurred")
        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    log.error("Malformed JSON or invalid field value: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .message("Invalid request body: " + ex.getMostSpecificCause().getMessage())
        .httpStatus(HttpStatus.BAD_REQUEST)
        .localDateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}