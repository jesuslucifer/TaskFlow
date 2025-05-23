package com.example.backend.controller;

import com.example.backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You are not authenticated");
        }

        User user = (User) authentication.getPrincipal();


        return ResponseEntity.ok(Map.of
                ("username", user.getUsername(),
                        "email", user.getEmail(),
                        "avatarUrl", user.getAvatarUrl()));
    }
}
