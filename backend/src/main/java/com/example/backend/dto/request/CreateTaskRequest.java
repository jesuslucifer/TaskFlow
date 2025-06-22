package com.example.backend.dto.request;

import com.example.backend.model.Priority;
import com.example.backend.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class CreateTaskRequest {
    private String name;
    private String description;
    private Status status;
    private Priority priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
}
