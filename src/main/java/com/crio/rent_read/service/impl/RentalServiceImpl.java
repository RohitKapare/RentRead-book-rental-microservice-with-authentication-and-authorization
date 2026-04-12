package com.crio.rent_read.service.impl;

import com.crio.rent_read.dto.response.RentalResponse;
import com.crio.rent_read.entity.Book;
import com.crio.rent_read.entity.Rental;
import com.crio.rent_read.entity.User;
import com.crio.rent_read.entity.enums.AvailabilityStatus;
import com.crio.rent_read.exception.BookNotAvailableException;
import com.crio.rent_read.exception.RentalLimitExceededException;
import com.crio.rent_read.exception.ResourceNotFoundException;
import com.crio.rent_read.mapper.RentalMapper;
import com.crio.rent_read.repository.BookRepository;
import com.crio.rent_read.repository.RentalRepository;
import com.crio.rent_read.repository.UserRepository;
import com.crio.rent_read.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {

  private static final int MAX_ACTIVE_RENTALS = 2;

  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;
  private final BookRepository bookRepository;
  private final RentalMapper rentalMapper;

  @Override
  @Transactional
  public RentalResponse rentBook(Long userId, Long bookId) {
    log.info("User {} attempting to rent book {}", userId, bookId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

    // Check rental limit
    long activeRentals = rentalRepository.countByUserIdAndReturnDateIsNull(userId);
    if (activeRentals >= MAX_ACTIVE_RENTALS) {
      log.warn("User {} has already reached maximum book rental limit", userId);
      throw new RentalLimitExceededException("User has already reached maximum book rental limit!");
    }

    // Check book availability
    if (book.getAvailabilityStatus() == AvailabilityStatus.NOT_AVAILABLE) {
      log.warn("Book {} is not available for rent", bookId);
      throw new BookNotAvailableException("Book is currently not available for rent");
    }

    // Update book availability
    book.setAvailabilityStatus(AvailabilityStatus.NOT_AVAILABLE);
    bookRepository.save(book);

    // Create rental
    Rental rental = Rental.builder()
        .user(user)
        .book(book)
        .rentedAt(LocalDate.now())
        .returnDate(null)
        .build();

    Rental savedRental = rentalRepository.save(rental);
    log.info("Book {} rented successfully by user {}. Rental id: {}", bookId, userId, savedRental.getId());

    return rentalMapper.toRentalResponse(savedRental);
  }

  @Override
  @Transactional
  public void returnBook(Long rentalId) {
    log.info("Returning rental with id: {}", rentalId);

    Rental rental = rentalRepository.findById(rentalId)
        .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + rentalId));

    if (rental.getReturnDate() != null) {
      throw new BookNotAvailableException("This book has already been returned");
    }

    // Update return date
    rental.setReturnDate(LocalDate.now());
    rentalRepository.save(rental);

    // Update book availability
    Book book = rental.getBook();
    book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    bookRepository.save(book);

    log.info("Rental {} returned successfully. Book {} is now available", rentalId, book.getId());
  }

  @Override
  public List<RentalResponse> getActiveRentals(Long userId) {
    log.info("Fetching active rentals for user: {}", userId);

    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    return rentalRepository.findByUserIdAndReturnDateIsNull(userId).stream()
        .map(rentalMapper::toRentalResponse)
        .collect(Collectors.toList());
  }
}
