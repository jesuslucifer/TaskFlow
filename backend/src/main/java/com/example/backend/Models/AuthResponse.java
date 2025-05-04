package com.example.backend.Models;

// Класс для ответа с токеном
public class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
