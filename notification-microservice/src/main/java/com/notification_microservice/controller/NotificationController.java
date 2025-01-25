package com.notification_microservice.controller;

import com.notification_microservice.request.EmailRequest;
import com.notification_microservice.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final INotificationService notificationService;

    @PostMapping("/support")
    public ResponseEntity<HttpStatus> sendSupportEmail(@RequestBody EmailRequest emailRequest  ,
                                                       @RequestHeader("${gateway.custom-headers.user-id}")Long userId){
        try {
            notificationService.sendSupportEmail(emailRequest, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
