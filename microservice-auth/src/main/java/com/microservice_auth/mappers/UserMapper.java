package com.microservice_auth.mappers;

import com.microservice_auth.dto.UserDto;
import com.microservice_auth.entity.User;
import com.microservice_auth.request.CreateUserRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

   // Convierte de Request a entidad
    User toEntity(CreateUserRequest userRequest);

}
