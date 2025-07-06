package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import com.example.backend.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class TaskDto {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private Priority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;

    public TaskDto() {}

    public TaskDto(Task task) {
        this.name = task.getName();
        this.description = task.getDescription();
        this.taskStatus = task.getTaskStatus();
        this.priority = task.getPriority();
        this.dateTo = task.getDateTo();
        this.timeLeft = task.getTimeLeft();
    }
}
