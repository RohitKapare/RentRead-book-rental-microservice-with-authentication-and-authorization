package com.crio.rent_read.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.crio.rent_read.dto.request.BookRequest;
import com.crio.rent_read.dto.response.BookResponse;
import com.crio.rent_read.entity.enums.AvailabilityStatus;
import com.crio.rent_read.entity.enums.Genre;
import com.crio.rent_read.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BookService bookService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("ADMIN should be able to create a book and receive 201")
  void testCreateBook_AdminSuccess() throws Exception {
    BookRequest request = BookRequest.builder()
        .title("Test Book")
        .author("Test Author")
        .genre(Genre.FICTION)
        .availabilityStatus(AvailabilityStatus.AVAILABLE)
        .build();

    BookResponse response = BookResponse.builder()
        .id(1L)
        .title("Test Book")
        .author("Test Author")
        .genre(Genre.FICTION)
        .availabilityStatus(AvailabilityStatus.AVAILABLE)
        .build();

    when(bookService.createBook(any(BookRequest.class))).thenReturn(response);

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Test Book"));
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("USER should receive 403 Forbidden when trying to create a book")
  void testCreateBook_UserForbidden() throws Exception {
    BookRequest request = BookRequest.builder()
        .title("Test Book")
        .author("Test Author")
        .genre(Genre.FICTION)
        .availabilityStatus(AvailabilityStatus.AVAILABLE)
        .build();

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("Should return all books with status 200")
  void testGetAllBooks_Success() throws Exception {
    BookResponse book1 = BookResponse.builder()
        .id(1L).title("Book 1").author("Author 1")
        .genre(Genre.FICTION).availabilityStatus(AvailabilityStatus.AVAILABLE).build();
    BookResponse book2 = BookResponse.builder()
        .id(2L).title("Book 2").author("Author 2")
        .genre(Genre.FANTASY).availabilityStatus(AvailabilityStatus.AVAILABLE).build();

    when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

    mockMvc.perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].title").value("Book 1"));
  }
}
