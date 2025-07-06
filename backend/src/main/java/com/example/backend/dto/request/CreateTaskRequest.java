package com.example.backend.dto.request;

import com.example.backend.model.Priority;
import com.example.backend.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class CreateTaskRequest {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private Priority priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
}
