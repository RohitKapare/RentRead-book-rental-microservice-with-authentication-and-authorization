package com.crio.rent_read.service.impl;

import com.crio.rent_read.dto.request.LoginRequest;
import com.crio.rent_read.dto.request.SignupRequest;
import com.crio.rent_read.dto.response.UserResponse;
import com.crio.rent_read.entity.User;
import com.crio.rent_read.entity.enums.Role;
import com.crio.rent_read.exception.DuplicateResourceException;
import com.crio.rent_read.mapper.UserMapper;
import com.crio.rent_read.repository.UserRepository;
import com.crio.rent_read.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;

  @Override
  public UserResponse register(SignupRequest signupRequest) {
    log.info("Registering new user with email: {}", signupRequest.getEmail());

    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      throw new DuplicateResourceException("User with email " + signupRequest.getEmail() + " already exists");
    }

    User user = userMapper.toUser(signupRequest);
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

    // Default role to USER if not specified
    if (signupRequest.getRole() == null) {
      user.setRole(Role.USER);
    }

    User savedUser = userRepository.save(user);
    log.info("User registered successfully with id: {}", savedUser.getId());

    return userMapper.toUserResponse(savedUser);
  }

  @Override
  public UserResponse login(LoginRequest loginRequest) {
    log.info("Login attempt for email: {}", loginRequest.getEmail());

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmail(),
              loginRequest.getPassword()
          )
      );
    } catch (BadCredentialsException e) {
      log.error("Login failed for email: {}", loginRequest.getEmail());
      throw new BadCredentialsException("Invalid email or password");
    }

    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

    log.info("User logged in successfully: {}", user.getEmail());
    return userMapper.toUserResponse(user);
  }
}