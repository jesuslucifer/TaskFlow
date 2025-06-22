package com.example.backend.dto.request;

import com.example.backend.model.ExecutorRole;
import lombok.Data;

@Data
public class ProjectExecutorRequest {
    private Long executorId;
    private ExecutorRole executorRole;
}
