package com.example.sender.controller;

import com.example.sender.services.AuthService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sender.dto.*;

import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register/user")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/signin/user")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
