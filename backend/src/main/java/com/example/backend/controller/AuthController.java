package com.example.backend.controller;

import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody SignInRequest signInRequest) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));


        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userService.getByUsername(signInRequest.getUsername());

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JwtResponse> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        var token = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
