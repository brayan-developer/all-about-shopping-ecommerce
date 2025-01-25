package com.microservice_auth.service;

import com.microservice_auth.dto.UserDto;
import com.microservice_auth.request.AuthRequest;

public interface IAuthService {

    String login(AuthRequest authRequest);

    String loginWithGoogle(String tokenId) throws Exception;

    UserDto validateToken(String token);

}
