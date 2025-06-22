package com.example.backend.dto.response;

import com.example.backend.model.ExecutorRole;
import com.example.backend.model.ProjectExecutor;
import lombok.Data;

@Data
public class ExecutorDto {
    UserDto user;
    ExecutorRole role;

    public ExecutorDto(ProjectExecutor executor) {
        this.user = new UserDto(executor.getUser());
        this.role = executor.getRole();
    }
}
