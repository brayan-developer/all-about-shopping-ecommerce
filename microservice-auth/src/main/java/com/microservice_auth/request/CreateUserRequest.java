package com.microservice_auth.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateUserRequest {


    @NotNull
    @Email
    @Size(min = 6)
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;

    @Size(max = 150)
    @NotNull
    private String fullName;

    @Size(max = 20)
    private String phoneNumber;

    private MultipartFile avatarFile;


}
