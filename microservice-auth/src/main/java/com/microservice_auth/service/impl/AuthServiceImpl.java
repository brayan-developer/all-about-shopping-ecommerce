package com.microservice_auth.service.impl;

import com.microservice_auth.exceptions.UnauthorizedAccessException;
import com.microservice_auth.service.GoogleAuthHandler;
import com.microservice_auth.dto.UserDto;
import com.microservice_auth.entity.User;
import com.microservice_auth.enums.AccountStatus;
import com.microservice_auth.request.AuthRequest;
import com.microservice_auth.service.IAuthService;
import com.microservice_auth.payload.ApiResponse;
import com.microservice_auth.service.IUserService;
import com.microservice_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.microservice_auth.constants.ErrorMessageConstants.*;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final GoogleAuthHandler googleAuthHandler;

    private final IUserService userService;

    @Override
    public String login(AuthRequest authRequest) throws AuthenticationException {
        User user = userService.getUserByEmail(authRequest.getEmail());

        handleAccountStatus(user.getAccountStatus());

        authenticate(authRequest);

        return jwtUtil.generateToken(String.valueOf(user.getId()));
    }


    @Override
    @Transactional
    public String loginWithGoogle(String tokenId) throws Exception {
        Long userId = googleAuthHandler.authenticateWithGoogle(tokenId);
        return jwtUtil.generateToken(String.valueOf(userId));

    }


    @Override
    public UserDto validateToken(String token) {
        String userId = jwtUtil.extractUserId(stripBearerPrefix(token));
        return userService.getUserById(Long.valueOf(userId));
    }

    private void handleAccountStatus(AccountStatus accountStatus) {
        switch (accountStatus) {
            case ACTIVE -> {}
            case PENDING_VERIFICATION -> throw new UnauthorizedAccessException(ACCOUNT_PENDING_VERIFICATION);
            case SUSPENDED -> throw new UnauthorizedAccessException(ACCOUNT_SUSPENDED);
            default -> throw new UnauthorizedAccessException(INVALID_ACCOUNT_STATUS);
        }
    }

    private String stripBearerPrefix(String token){
        return (token.startsWith("Bearer ")) ?
                token.substring(7)
                : token;
    }



    private void authenticate(AuthRequest authRequest) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
    }


}
