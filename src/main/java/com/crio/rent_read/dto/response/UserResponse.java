package com.crio.rent_read.dto.response;

import com.crio.rent_read.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private Role role;
}