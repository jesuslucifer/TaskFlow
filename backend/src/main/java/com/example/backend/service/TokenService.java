package com.example.backend.service;

import com.example.backend.model.Token;
import com.example.backend.model.User;
import com.example.backend.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();

        token.setRefreshToken(refreshToken);

        token.setAccessToken(accessToken);

        token.setUser(user);

        tokenRepository.save(token);
    }
}
