package com.crio.rent_read.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.crio.rent_read.dto.response.BookResponse;
import com.crio.rent_read.dto.response.RentalResponse;
import com.crio.rent_read.entity.enums.AvailabilityStatus;
import com.crio.rent_read.entity.enums.Genre;
import com.crio.rent_read.exception.RentalLimitExceededException;
import com.crio.rent_read.service.RentalService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RentalControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private RentalService rentalService;

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("USER should be able to rent a book and receive 201")
  void testRentBook_Success() throws Exception {
    BookResponse bookResponse = BookResponse.builder()
        .id(1L).title("Test Book").author("Test Author")
        .genre(Genre.FICTION).availabilityStatus(AvailabilityStatus.NOT_AVAILABLE).build();

    RentalResponse response = RentalResponse.builder()
        .id(1L)
        .book(bookResponse)
        .rentedAt(LocalDate.now())
        .returnDate(null)
        .build();

    when(rentalService.rentBook(1L, 1L)).thenReturn(response);

    mockMvc.perform(post("/rentals/users/1/books/1"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.book.title").value("Test Book"))
        .andExpect(jsonPath("$.returnDate").isEmpty());
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("Should return 400 Bad Request when rental limit is exceeded")
  void testRentBook_RentalLimitExceeded() throws Exception {
    when(rentalService.rentBook(1L, 3L))
        .thenThrow(new RentalLimitExceededException(
            "User has already reached maximum book rental limit!"));

    mockMvc.perform(post("/rentals/users/1/books/3"))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message").value("User has already reached maximum book rental limit!"));
  }
}
