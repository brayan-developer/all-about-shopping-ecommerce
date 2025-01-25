package com.notification_microservice.service;

import com.notification_microservice.request.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BuildEmailBodyService {

    @Value("${frontend.urls.activate-account}")
    private String activateAccountUrl;
    @Value("${frontend.urls.report-account}")
    private String reportAccountUrl;
    public String buildVerificationEmailBody(String email, String tokenJwt) {
        StringBuilder bodyHtml = new StringBuilder();

        bodyHtml.append("<h2> Hello ").append(email).append("</h2>")
                .append("<p>Thank you for registering at 'Todo Sobre Compras'.<br>")
                .append("Please click the link below to verify your email address and activate your account:</p><br>")
                .append("<a href='").append(activateAccountUrl).append(tokenJwt).append("'")
                .append(" style='padding:10px 20px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:5px;'>")
                .append("Verify Email</a><br><br>")
                .append("<div>If you did not register with this email, <a href='")
                .append(reportAccountUrl).append(tokenJwt).append("'>report the account of the user who tried to register.</a> <br/>")
                .append("Best regards,<br> All about Shopping</div>");

        return bodyHtml.toString();
    }

    public String buildSupportEmailBody(EmailRequest emailRequest, Long userId) {
        StringBuilder bodyHtml = new StringBuilder();

        bodyHtml
                .append("<p>Subject: ").append(emailRequest.getMailSubject()).append("<br>")
                .append("userId: ").append(userId).append("<br>")
                .append("emailUser: ").append(emailRequest.getSender()).append("</p> <br>")
                .append("message: ").append(emailRequest.getMessage());

        return bodyHtml.toString();
    }
}
