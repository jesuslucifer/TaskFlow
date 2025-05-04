package com.example.backend.Service;

import com.example.backend.Jwt.Util.JwtUtil;
import com.example.backend.Models.User;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Optional<String> login(String username, String email, String password) {
        return findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .filter(user -> user.getEmail().equals(email)) // проверка email
                .map(user -> jwtUtil.generateToken(username));
    }

}

