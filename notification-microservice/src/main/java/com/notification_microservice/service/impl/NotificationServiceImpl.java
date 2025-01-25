package com.notification_microservice.service.impl;

import com.notification_microservice.request.EmailRequest;
import com.notification_microservice.service.BuildEmailBodyService;
import com.notification_microservice.service.EmailSenderService;
import com.notification_microservice.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements INotificationService {

    private final EmailSenderService emailSenderService;

    private final BuildEmailBodyService buildEmailBodyService;

    @Value("${spring.mail.username}")
    private String addressed;

    @Override
    public void sendRegistrationVerificationEmail(String email, String token) {
        try {
            emailSenderService.sendEmail(email , "Tsc - Email Verification",
                    buildEmailBodyService.buildVerificationEmailBody(email, token));
        }catch (Exception e){
            throw new RuntimeException("An a error occurred: " + e);
        }

    }

    @Override
    public void sendSupportEmail(EmailRequest emailRequest, Long userId) throws Exception {
        emailSenderService.sendEmail(addressed, "Tsc - New Support Email",
                buildEmailBodyService.buildSupportEmailBody(emailRequest, userId));
    }

}
