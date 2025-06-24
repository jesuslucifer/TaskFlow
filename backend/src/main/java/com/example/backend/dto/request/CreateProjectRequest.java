package com.example.backend.dto.request;

import com.example.backend.model.Category;
import com.example.backend.model.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CreateProjectRequest {
    private String name;
    private String description;
    private Priority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
    private List<Category> categories;
}
