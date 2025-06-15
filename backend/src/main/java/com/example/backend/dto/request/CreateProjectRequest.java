package com.example.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class CreateProjectRequest {
    private String name;
    private String description;
    //private ProjectStatus status;
    //private ProjectPriority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
    private Integer userId;
}
