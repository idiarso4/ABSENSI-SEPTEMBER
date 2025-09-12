package com.simsekolah.controller;

import com.simsekolah.dto.request.LoginRequest;
import com.simsekolah.dto.response.AuthenticationResponse;
import com.simsekolah.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// A minimal stub to allow AuthControllerTest to compile independently.
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping({"/api/v1/auth/login", "/api/auth/login"})
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

