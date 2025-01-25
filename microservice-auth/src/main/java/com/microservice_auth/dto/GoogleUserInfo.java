package com.microservice_auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserInfo {
    private String sub; // ID único del usuario de Google
    private String email;
    private String name;
    private String picture; // URL de la foto de perfil
    private String locale;
    private boolean email_verified;

}