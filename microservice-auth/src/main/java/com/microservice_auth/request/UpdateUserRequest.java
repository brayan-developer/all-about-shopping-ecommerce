package com.microservice_auth.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateUserRequest {

    private String fullName;

    private String phoneNumber;

    private boolean removeAvatar;

    private MultipartFile avatarFile;
}
