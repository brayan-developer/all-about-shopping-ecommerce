package com.notification_microservice.service;

import com.notification_microservice.request.EmailRequest;

public interface INotificationService {

    void sendRegistrationVerificationEmail(String email, String token);


    void sendSupportEmail(EmailRequest emailRequest, Long userId) throws Exception;
}
