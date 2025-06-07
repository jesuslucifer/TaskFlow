package com.example.backend.dto.response;

import com.example.backend.model.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDto {
    @Hidden
    private Long id;
    @Schema(description = "Username пользователя", example = "test")
    private String username;
    @Schema(description = "Email пользователя", example = "test@test.ru")
    private String email;
    @Schema(description = "Аватар пользователя")
    private String avatarUrl;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
    }
}
