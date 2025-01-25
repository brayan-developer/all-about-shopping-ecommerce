package com.microservice_auth.service;


import com.microservice_auth.dto.UserDto;
import com.microservice_auth.entity.Role;
import com.microservice_auth.entity.User;
import com.microservice_auth.enums.AccountStatus;
import com.microservice_auth.request.CreateUserRequest;
import com.microservice_auth.request.UpdateUserRequest;

public interface IUserService {

    UserDto getUserById(Long id);
    UserDto createUser(CreateUserRequest userRequest);

    UserDto updateUser(Long userId , UpdateUserRequest userRequest);

    User getUserByEmail(String email);
    void updateUserAccountStatus(Long userId, AccountStatus accountStatus);
    Role getDefaultUserRole();


}
