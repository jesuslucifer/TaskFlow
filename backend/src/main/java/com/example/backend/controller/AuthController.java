package com.example.backend.controller;

import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.service.JwtService;
import com.example.backend.service.TokenService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody SignInRequest signInRequest) throws Exception {

        User user = userService.getByUsernameOrEmail(signInRequest.getUsernameOrEmail(), signInRequest.getUsernameOrEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), signInRequest.getPassword()));


        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.saveToken(accessToken, refreshToken, user);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        return ResponseEntity.ok("Success registered");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) throws Exception {
        String requestRefreshTokenRefreshToken = request.getRefreshToken();

        String username = jwtService.extractUsername(requestRefreshTokenRefreshToken);

        User user = userService.getByUsername(username);

        if (!jwtService.validateRefreshToken(requestRefreshTokenRefreshToken, user)) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.saveToken(accessToken, refreshToken, user);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }
}
