package com.microservice_auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.microservice_auth.entity.User;
import com.microservice_auth.enums.AccountStatus;
import com.microservice_auth.exceptions.UnauthorizedAccessException;
import com.microservice_auth.repository.IUserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.microservice_auth.constants.ErrorMessageConstants.*;

@RequiredArgsConstructor
@Service
public class GoogleAuthHandler {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();

    private GoogleIdTokenVerifier verifier;

    private final IUserRepository userRepository;


    private final IUserService userService;

    @PostConstruct
    public void initVerifier() throws Exception {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        this.verifier = new GoogleIdTokenVerifier.Builder(transport, GSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }


    public Long authenticateWithGoogle(String idToken) throws Exception {
        GoogleIdToken.Payload payload = validateGoogleToken(idToken);

        User user = handleSaveGoogleUser(payload);
        return user.getId();
    }

    public GoogleIdToken.Payload validateGoogleToken(String idTokenString) throws Exception {

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            if (payload.getExpirationTimeSeconds() < System.currentTimeMillis() / 1000) {
                throw new JwtException(TOKEN_EXPIRED);
            }

            return payload;
        }

        throw new JwtException(INVALID_TOKEN);

    }

    private User handleSaveGoogleUser(GoogleIdToken.Payload payload){
        Optional<User> userOptional = userRepository.findByEmail(payload.getEmail());

        handleAccountStatus(userOptional.get());

        return userOptional.orElseGet(() -> userRepository.save(
                User.builder()
                        .email(payload.getEmail())
                        .fullName((String) payload.get("name"))
                        .phoneNumber((String) payload.get("phone_number"))
                        .avatarUrl((String) payload.get("picture"))
                        .accountStatus(AccountStatus.ACTIVE)
                        .role(userService.getDefaultUserRole())
                        .build()
        ));
    }

    private void handleAccountStatus(User user) {
        switch (user.getAccountStatus()) {
            case PENDING_VERIFICATION -> {
                user.setAccountStatus(AccountStatus.ACTIVE);
                userRepository.save(user);
            }
            case SUSPENDED -> throw new UnauthorizedAccessException(ACCOUNT_SUSPENDED);
            case BANNED -> throw new UnauthorizedAccessException(ACCOUNT_BANNED);
            default -> throw new UnauthorizedAccessException(INVALID_ACCOUNT_STATUS);
        }
    }

}
