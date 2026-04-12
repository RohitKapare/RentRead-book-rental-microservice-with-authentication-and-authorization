package com.crio.rent_read.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.crio.rent_read.dto.request.LoginRequest;
import com.crio.rent_read.dto.request.SignupRequest;
import com.crio.rent_read.dto.response.UserResponse;
import com.crio.rent_read.entity.enums.Role;
import com.crio.rent_read.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Should register a USER successfully and return 201")
  void testRegisterUser_Success() throws Exception {
    SignupRequest request = SignupRequest.builder()
        .email("test@example.com")
        .password("password123")
        .firstName("John")
        .lastName("Doe")
        .build();

    UserResponse response = UserResponse.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("test@example.com")
        .role(Role.USER)
        .build();

    when(authService.register(any(SignupRequest.class))).thenReturn(response);

    mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.role").value("USER"));
  }

  @Test
  @DisplayName("Should register an ADMIN successfully and return 201")
  void testRegisterAdmin_Success() throws Exception {
    SignupRequest request = SignupRequest.builder()
        .email("admin@rentread.com")
        .password("admin123456")
        .firstName("admin")
        .lastName("test")
        .role(Role.ADMIN)
        .build();

    UserResponse response = UserResponse.builder()
        .id(2L)
        .firstName("admin")
        .lastName("test")
        .email("admin@rentread.com")
        .role(Role.ADMIN)
        .build();

    when(authService.register(any(SignupRequest.class))).thenReturn(response);

    mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  @DisplayName("Should login successfully and return 200 with user details")
  void testLogin_Success() throws Exception {
    LoginRequest request = LoginRequest.builder()
        .email("test@example.com")
        .password("password123")
        .build();

    UserResponse response = UserResponse.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("test@example.com")
        .role(Role.USER)
        .build();

    when(authService.login(any(LoginRequest.class))).thenReturn(response);

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }
}

