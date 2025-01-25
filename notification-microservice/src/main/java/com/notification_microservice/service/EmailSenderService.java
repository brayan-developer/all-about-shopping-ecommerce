package com.notification_microservice.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class EmailSenderService {


    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.logo-url}")
    private String logoUrl;

    public void sendEmail(String addressee, String subject, String content) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(addressee);
        helper.setFrom(fromEmail, "Todo Sobre Compras");

        helper.setSubject(subject);
        helper.setText(buildHtmlTemplate(content), true);

        mailSender.send(message);
    }

    private String buildHtmlTemplate(String body){
        return new StringBuilder()
                .append("<!DOCTYPE PUBLIC “-//W3C//DTD XHTML 1.0 Transitional//EN” “https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd”>")
                .append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\">")
                .append("<title></title>")
                .append("</head>")
                .append("<body>")
                .append("<br/> <img src='").append(logoUrl).append("'/> <br/>")
                .append(body)
                .append("</body>")
                .append("</html>")
                .toString();
    }


}
