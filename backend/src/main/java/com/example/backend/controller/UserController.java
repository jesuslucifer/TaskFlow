package com.example.backend.controller;

import com.example.backend.dto.response.ErrorResponse;
import com.example.backend.dto.response.UserDto;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Управление пользователями")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "Получить текущего пользователя",
            description = "Возвращает текущего пользователя"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(Map.of
                ("username", user.getUsername(),
                        "email", user.getEmail(),
                        "avatarUrl", user.getAvatarUrl()));
    }

    @PostMapping("/avatar")
    @Operation(
            summary = "Смена аватара",
            description = "Позволяет сменить аватар пользователю"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Аватар обновлен"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Ошибка смены аватара"
            )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> updateAvatarUrl(@RequestParam("file") MultipartFile avatarUrlRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        userService.updateAvatar(user.getId(), avatarUrlRequest);

        return ResponseEntity.ok("Successfully updated avatar url");
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение пользователя по id",
            description = "Возвращает пользователя по его идентификатору"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(Map.of
                ("username", user.getUsername(),
                        "email", user.getEmail(),
                        "avatarUrl", user.getAvatarUrl()));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает всех пользователей"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователи найдены",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getUsers() {
        List<UserDto> userDto = userService.getAll();

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/username/{username}")
    @Operation(
            summary = "Получение пользователя по username",
            description = "Возвращает пользователя по его username"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);

        return ResponseEntity.ok(Map.of
                ("username", user.getUsername(),
                        "email", user.getEmail(),
                        "avatarUrl", user.getAvatarUrl()));
    }
    
}
