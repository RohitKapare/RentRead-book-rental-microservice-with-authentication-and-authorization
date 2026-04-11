package com.crio.rent_read.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
  private String message;
  private HttpStatus httpStatus;
  private LocalDateTime localDateTime;
}