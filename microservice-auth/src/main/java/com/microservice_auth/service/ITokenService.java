package com.microservice_auth.service;

import com.microservice_auth.payload.ApiResponse;

public interface ITokenService {
    ApiResponse resendVerificationToken(String email);

    ApiResponse activateUserAccount(String token);
    ApiResponse reportUserAccount(String token);
}
