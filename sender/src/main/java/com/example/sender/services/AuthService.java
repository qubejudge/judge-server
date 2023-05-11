package com.example.sender.services;

import com.example.sender.dto.LoginRequest;
import com.example.sender.dto.LoginResponse;
import com.example.sender.dto.RegisterRequest;
import com.example.sender.dto.RegisterResponse;
import com.example.sender.entity.Role;
import com.example.sender.entity.User;
import com.example.sender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;

    public ResponseEntity<RegisterResponse>register(RegisterRequest request)
    {
        var user = User.builder()
                .role(Role.USER)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

        try {
            userRepository.save(user);
        }catch (Exception r)
        {
            return new ResponseEntity<>(RegisterResponse.builder().message("There was an error while registering the user").build(), HttpStatusCode.valueOf(200));
        }

        var jwtToken = jwtService.generateToken(user);

        return new ResponseEntity<>(RegisterResponse.builder().token(jwtToken).message("User Registered Successfully").build(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch(Exception e){
            return new ResponseEntity<LoginResponse>(LoginResponse.builder().message("Login Failed").build(), HttpStatusCode.valueOf(403));
        }

        var jwtToken = jwtService.generateToken(user);
        return new ResponseEntity<>(LoginResponse.builder().token(jwtToken).message("User Logged in Successfully").build(), HttpStatusCode.valueOf(200));

    }
}
