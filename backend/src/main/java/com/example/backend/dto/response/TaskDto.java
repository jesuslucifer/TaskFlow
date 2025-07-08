package com.example.backend.dto.response;

import com.example.backend.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TaskDto {
    private long id;
    private String name;
    private String description;
    private TaskStatus status;
    private Priority priority;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
    private UserDto createUser;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreate;
    private List<TaskCategoryDto> categories;

    public TaskDto() {}

    public TaskDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getTaskStatus();
        this.priority = task.getPriority();
        this.dateTo = task.getDateTo();
        this.timeLeft = task.getTimeLeft();
        this.createUser = new UserDto(task.getCreateUser());
        this.dateCreate = task.getDateCreate();
        this.categories = task.getCategories()
                .stream()
                .map(TaskCategoryDto::new)
                .toList();
    }
}
