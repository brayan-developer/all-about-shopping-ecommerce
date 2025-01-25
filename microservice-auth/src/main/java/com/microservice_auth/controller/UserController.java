package com.microservice_auth.controller;


import com.microservice_auth.dto.UserDto;
import com.microservice_auth.request.CreateUserRequest;
import com.microservice_auth.request.UpdateUserRequest;
import com.microservice_auth.service.IUserService;
import com.microservice_auth.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    public final IUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> findUserById(@RequestHeader("${gateway.custom-headers.user-id}") String userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(Long.valueOf(userId))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @ModelAttribute CreateUserRequest userRequest){
        UserDto userSaved = userService.createUser(userRequest);
        return new ResponseEntity<>(ApiResponse.success(userSaved), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader("${gateway.custom-headers.user-id}") Long userId ,
                                        @Valid @ModelAttribute UpdateUserRequest userRequest) {

        UserDto userSaved = userService.updateUser(userId, userRequest);
        return new ResponseEntity<>(ApiResponse.success(userSaved), HttpStatus.OK);

    }

}
