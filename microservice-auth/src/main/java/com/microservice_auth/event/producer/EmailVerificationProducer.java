package com.microservice_auth.event.producer;

import com.microservice_auth.entity.User;
import com.microservice_auth.entity.VerificationToken;
import com.microservice_auth.repository.ITokenRepository;
import com.microservice_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.microservice_auth.constants.GeneralConstants.EMAIL_TOKEN_EXPIRATION_HOURS;

@RequiredArgsConstructor
@Service
public class EmailVerificationProducer {

    @Value("${kafka.topics.email-verification}")
    private String topicEmailVerification;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ITokenRepository verificationTokenRepository;
    private final JwtUtil jwtUtil;


    @Transactional
    public void sendVerificationTokenByEmail(User user) {
        String tokenJwt = jwtUtil.generateEmailVerificationToken(user.getEmail());

        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .token(tokenJwt)
                .expiresAt(LocalDateTime.now().plusHours(EMAIL_TOKEN_EXPIRATION_HOURS))
                .used(false)
                .build();
        verificationTokenRepository.save(verificationToken);

        kafkaTemplate.send(topicEmailVerification,user.getEmail(), tokenJwt);

    }
}
