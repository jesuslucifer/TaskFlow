package com.example.backend.dto.request;

import com.example.backend.model.Priority;
import com.example.backend.model.Status;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class CreateSubtaskRequest {
    private String name;
    private String description;
    private Status status;
    private Priority priority;
}
