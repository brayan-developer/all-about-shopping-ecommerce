package com.microservice.gateway.exception;


import com.microservice.gateway.payload.ApiResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.microservice.gateway.constants.ErrorMessageConstants.*;


@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException ex) {
        String message = ex.getMessage();
        HttpStatus status = switch (message) {
            case TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
            case MALFORMED_TOKEN, INVALID_TOKEN_SIGNATURE, INVALID_TOKEN -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.FORBIDDEN;
        };

        return new ResponseEntity<>(ApiResponse.error(message), status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }


}