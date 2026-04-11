package com.crio.rent_read.config;

import com.crio.rent_read.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionCreationPolicy(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/books/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/books/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
            .requestMatchers("/rentals/**").authenticated()
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              ErrorResponse errorResponse = ErrorResponse.builder()
                  .message("Access Denied: You don't have permission to perform this action")
                  .httpStatus(HttpStatus.FORBIDDEN)
                  .localDateTime(LocalDateTime.now())
                  .build();
              ObjectMapper mapper = new ObjectMapper();
              mapper.registerModule(new JavaTimeModule());
              response.getWriter().write(mapper.writeValueAsString(errorResponse));
            })
        );

    return http.build();
  }
}