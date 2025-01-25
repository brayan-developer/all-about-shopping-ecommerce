package com.microservice_auth.controller;


import com.microservice_auth.service.ITokenService;
import com.microservice_auth.payload.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tokens")
public class VerificationTokenController {

    private final ITokenService tokenService;

    @GetMapping
    public ResponseEntity<ApiResponse> activateUserAccount(@RequestParam("token") String token){
        ApiResponse apiResponse = tokenService.activateUserAccount(token);
        return new ResponseEntity<>(apiResponse, defineHttpStatus(apiResponse));
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse> resendVerificationToken(@Valid @Email @RequestParam String email) {
        ApiResponse apiResponse = tokenService.resendVerificationToken(email);
        return new ResponseEntity<>(apiResponse, defineHttpStatus(apiResponse));
    }

    @PatchMapping("/report")
    public ResponseEntity<ApiResponse> reportUserAccount(@RequestParam("token") String token) {
        ApiResponse apiResponse = tokenService.reportUserAccount(token);
        return new ResponseEntity<>( apiResponse, defineHttpStatus(apiResponse));
    }

    private HttpStatus defineHttpStatus(ApiResponse apiResponse){
        return apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

}
