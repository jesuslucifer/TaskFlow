package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Project;
import com.example.backend.model.ProjectCategories;
import com.example.backend.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

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
    }
}
