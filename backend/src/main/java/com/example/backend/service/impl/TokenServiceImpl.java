package com.example.backend.service.impl;

import com.example.backend.model.Token;
import com.example.backend.model.User;
import com.example.backend.repository.TokenRepository;
import com.example.backend.service.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public void saveToken(String refreshToken, User user) {
        Token token = new Token();

        token.setRefreshToken(refreshToken);

        token.setUser(user);

        tokenRepository.save(token);
    }

    @Override
    public void removeToken(User user) {

        List<Token> tokens = tokenRepository.findAllByUserId(user.getId());

        tokens.forEach(token -> token.setAvailable(false));

        tokenRepository.saveAll(tokens);
    }
}
