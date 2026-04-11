package com.crio.rent_read.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalResponse {
  private Long id;
  private BookResponse book;
  private LocalDate rentedAt;
  private LocalDate returnDate;
}
