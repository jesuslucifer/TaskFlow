package com.example.backend.dto.response;

import com.example.backend.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private Status status;
    private Priority priority;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateTo;
    private LocalTime timeLeft;
    private UserDto createUser;
    private ProjectCategories category;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreate;
    private List<ExecutorDto> executors;

    public ProjectDto() {}

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.status = project.getStatus();
        this.priority = project.getPriority();
        this.dateTo = project.getDateTo();
        this.timeLeft = project.getTimeLeft();
        this.createUser = new UserDto(project.getCreateUser());
        this.category = project.getCategory();
        this.dateCreate = project.getDateCreate();
        this.executors = project.getExecutors()
                .stream()
                .map(ExecutorDto::new)
                .collect(Collectors.toList());
    }
}
