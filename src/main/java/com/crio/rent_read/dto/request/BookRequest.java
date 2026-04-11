package com.crio.rent_read.dto.request;

import com.crio.rent_read.entity.enums.AvailabilityStatus;
import com.crio.rent_read.entity.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Author is required")
  private String author;

  @NotNull(message = "Genre is required")
  private Genre genre;

  @NotNull(message = "Availability status is required")
  private AvailabilityStatus availabilityStatus;
}