package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data

public class TaskDto {
    private String name;
    private String description;
    private Status status;
    private Priority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
}
