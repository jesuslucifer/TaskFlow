package com.example.backend.controller;

import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.dto.response.UserDto;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> updateAvatarUrl(@RequestParam("file") MultipartFile avatarUrlRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        userService.updateAvatar(user.getId(), avatarUrlRequest);

        return ResponseEntity.ok(new SuccessResponse(
                "Аватар обновлен",
                HttpStatus.OK
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(new UserDto(user));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers() {
        List<UserDto> userDto = userService.getAll();

        return ResponseEntity.ok(userDto);
    }
}
