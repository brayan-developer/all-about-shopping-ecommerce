package com.microservice_auth.service.impl;

import com.microservice_auth.entity.User;
import com.microservice_auth.entity.VerificationToken;
import com.microservice_auth.enums.AccountStatus;
import com.microservice_auth.exceptions.RateLimitException;
import com.microservice_auth.exceptions.ResourceNotFoundException;
import com.microservice_auth.event.producer.EmailVerificationProducer;
import com.microservice_auth.repository.ITokenRepository;
import com.microservice_auth.service.ITokenService;
import com.microservice_auth.payload.ApiResponse;
import com.microservice_auth.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.microservice_auth.constants.ErrorMessageConstants.*;
import static com.microservice_auth.constants.GeneralConstants.*;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final ITokenRepository verificationTokenRepository;
    private final EmailVerificationProducer emailVerificationProducer;

    private final IUserService userService;



    private boolean isTokenInvalidOrExpired(VerificationToken tokenEntity) {
        return tokenEntity.isUsed() || tokenEntity.getExpiresAt().isBefore(LocalDateTime.now());
    }

    @Transactional
    @Override
    public ApiResponse activateUserAccount(String token) {
            VerificationToken verificationToken = getByToken(token);

            if (isTokenInvalidOrExpired(verificationToken)) {
                return ApiResponse.error(TOKEN_USED_OR_EXPIRED);
            }

            userService.updateUserAccountStatus(verificationToken.getUser().getId(), AccountStatus.ACTIVE);

            verificationToken.setUsed(true);
            verificationTokenRepository.save(verificationToken);

            return ApiResponse.success("Token validated successfully");
    }


    @Transactional
    @Override
    public ApiResponse resendVerificationToken(String email){
            User user = userService.getUserByEmail(email);

            if(user.getAccountStatus() == AccountStatus.ACTIVE){
                return ApiResponse.error(ACCOUNT_ALREADY_ACTIVE);
            }

            var latestTokenOpt = verificationTokenRepository.findByUser(user);
            if(latestTokenOpt.isPresent()){
                VerificationToken latestToken = latestTokenOpt.get();

                long timeElapsed = Duration.between(latestToken.getCreatedAt(), LocalDateTime.now()).toMinutes();
                if (timeElapsed < 5) {
                    throw new RateLimitException(String.format(RESEND_EMAIL_RATE_LIMIT, (5 - timeElapsed)));
                }
            }

            // Clean old tokens
            verificationTokenRepository.deleteByUser(user);

            emailVerificationProducer.sendVerificationTokenByEmail(user);
            return ApiResponse.success();

    }

    @Transactional
    @Override
    public ApiResponse reportUserAccount(String token) {
        VerificationToken verificationToken = getByToken(token);

        if(isTokenInvalidOrExpired(verificationToken)){
            return ApiResponse.error(TOKEN_USED_OR_EXPIRED);
        }

        userService.updateUserAccountStatus(verificationToken.getUser().getId(), AccountStatus.SUSPENDED);
        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return ApiResponse.success("User reported successfully");
    }

    private VerificationToken getByToken(String token){
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(VerificationToken.class.getSimpleName(),
                        FIELD_TOKEN,token));
    }


}
