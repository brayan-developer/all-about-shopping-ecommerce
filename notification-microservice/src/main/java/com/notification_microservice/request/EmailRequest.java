package com.notification_microservice.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {

    private String sender;

    private String mailSubject;

    private String message;

}
