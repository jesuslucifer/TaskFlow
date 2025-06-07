package com.example.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    @Schema(description = "Access токен")
    String accessToken;
    @Schema(description = "Refresh токен")
    String refreshToken;
}
