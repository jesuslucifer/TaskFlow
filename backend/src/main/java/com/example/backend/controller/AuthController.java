package com.example.backend.controller;

import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.response.ErrorResponse;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.exception.AuthenticationFailedException;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.service.JwtService;
import com.example.backend.service.TokenService;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authorization API", description = "Авторизация пользователей")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация",
            description = "Авторизация по логину/email + пароль"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Авторизация успешна",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(
            responseCode = "403",
            description = "Ошибка авторизации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JwtResponse> login(@RequestBody SignInRequest signInRequest) throws Exception {
        try {
            User user = userService.getByUsernameOrEmail(signInRequest.getUsernameOrEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), signInRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtService.generateAccessToken(user);

            String refreshToken = jwtService.generateRefreshToken(user);

            tokenService.saveToken(refreshToken, user);

            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
        } catch (Exception e) {
            throw new AuthenticationFailedException();
        }
    }

    @PostMapping("/sign-up")
    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрация по логин + email + пароль"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Регистрация успешна"
            )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка регистрации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .avatarUrl("http://localhost:8080/uploads/avatars/default.jpg")
                .build();

        userService.create(user);

        return ResponseEntity.ok("Success registered");
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Обновление access токена",
            description = "Обновление access токена с помощью refresh токена"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Токен обновлен",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка обновления",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) throws Exception {
        String requestRefreshTokenRefreshToken = request.getRefreshToken();

        String username = jwtService.extractUsername(requestRefreshTokenRefreshToken);

        User user = userService.getByUsername(username);

        if (!jwtService.validateRefreshToken(requestRefreshTokenRefreshToken, user)) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.removeToken(user);

        tokenService.saveToken(refreshToken, user);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Выход",
            description = "Выход пользователя из аккаунта"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешный выход"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Ошибка выхода",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        tokenService.removeToken(user);

        return ResponseEntity.ok("Successfully logged out");
    }
}
