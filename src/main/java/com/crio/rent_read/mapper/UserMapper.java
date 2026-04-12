package com.crio.rent_read.mapper;

import com.crio.rent_read.dto.request.SignupRequest;
import com.crio.rent_read.dto.response.UserResponse;
import com.crio.rent_read.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse toUserResponse(User user);

  @Mapping(target = "id", ignore = true)
  User toUser(SignupRequest signupRequest);

}