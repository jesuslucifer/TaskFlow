package com.example.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "Сообщение ошибки", example = "Пользователь не найден")
    private String message;
    @Schema(description = "Код ошибки")
    private HttpStatus status;
}
