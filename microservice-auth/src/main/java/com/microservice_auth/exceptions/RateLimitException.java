package com.microservice_auth.exceptions;

public class RateLimitException extends RuntimeException{

    public RateLimitException(String message){
        super(message);
    }

}
