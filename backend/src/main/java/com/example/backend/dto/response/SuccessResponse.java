package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private String message;
    private HttpStatus status;
}
