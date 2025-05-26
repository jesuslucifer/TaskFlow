package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping("/avatar")
    public ResponseEntity<String> updateAvatarUrl(@RequestParam("file") MultipartFile avatarUrlRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You are not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        userService.updateAvatar(user.getId(), avatarUrlRequest);

        return ResponseEntity.ok("Successfully updated avatar url");
    }
    
}
