package com.example.backend.service;

import com.example.backend.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String generateToken(User user, Long expiration);

    Boolean validateToken(String token, UserDetails userDetails);

    Boolean validateRefreshToken(String token, UserDetails userDetails);

    String extractUsername(String token);
}
