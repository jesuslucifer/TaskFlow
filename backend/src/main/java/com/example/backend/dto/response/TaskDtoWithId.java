package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Status;
import com.example.backend.model.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class TaskDtoWithId {
    private Long id;
    private String name;
    private String description;
    private Status status;
    private Priority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;

    public TaskDtoWithId() {}

    public TaskDtoWithId(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.dateTo = task.getDateTo();
        this.timeLeft = task.getTimeLeft();
    }
}
