package com.microservice_auth.dto;


import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserDto {


    private String email;
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;

}
