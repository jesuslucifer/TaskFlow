package com.example.backend.service;

import com.example.backend.model.User;

public interface TokenService {

    void saveToken(String refreshToken, User user);

    void removeToken(User user);
}
