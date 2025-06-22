package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Status;
import com.example.backend.model.Task;
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
    private LocalDate dateTo;
    private LocalTime timeLeft;

    public TaskDto() {}

    public TaskDto(Task task) {
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.dateTo = task.getDateTo();
        this.timeLeft = task.getTimeLeft();
    }
}
