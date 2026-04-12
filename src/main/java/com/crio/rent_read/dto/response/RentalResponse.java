package com.crio.rent_read.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
