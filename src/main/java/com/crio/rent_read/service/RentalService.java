package com.crio.rent_read.service;

import com.crio.rent_read.dto.response.RentalResponse;
import java.util.List;

public interface RentalService {

  RentalResponse rentBook(Long userId, Long bookId);

  void returnBook(Long rentalId);

  List<RentalResponse> getActiveRentals(Long userId);
}