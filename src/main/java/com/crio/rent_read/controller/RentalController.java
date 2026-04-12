package com.crio.rent_read.controller;

import com.crio.rent_read.dto.response.RentalResponse;
import com.crio.rent_read.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@Slf4j
public class RentalController {

  private final RentalService rentalService;

  @PostMapping("/users/{userId}/books/{bookId}")
  public ResponseEntity<RentalResponse> rentBook(@PathVariable Long userId,
      @PathVariable Long bookId) {
    log.info("Rent book request: user={}, book={}", userId, bookId);
    RentalResponse response = rentalService.rentBook(userId, bookId);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping("/{rentalId}")
  public ResponseEntity<Void> returnBook(@PathVariable Long rentalId) {
    log.info("Return book request for rental: {}", rentalId);
    rentalService.returnBook(rentalId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/active-rentals/users/{userId}")
  public ResponseEntity<List<RentalResponse>> getActiveRentals(@PathVariable Long userId) {
    log.info("Get active rentals request for user: {}", userId);
    List<RentalResponse> rentals = rentalService.getActiveRentals(userId);
    return ResponseEntity.ok(rentals);
  }
}