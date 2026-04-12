package com.crio.rent_read.controller;

import com.crio.rent_read.dto.request.LoginRequest;
import com.crio.rent_read.dto.request.SignupRequest;
import com.crio.rent_read.dto.response.UserResponse;
import com.crio.rent_read.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody SignupRequest signupRequest) {
    log.info("Signup request received for email: {}", signupRequest.getEmail());
    UserResponse response = authService.register(signupRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    log.info("Login request received for email: {}", loginRequest.getEmail());
    UserResponse response = authService.login(loginRequest);
    return ResponseEntity.ok(response);
  }
}