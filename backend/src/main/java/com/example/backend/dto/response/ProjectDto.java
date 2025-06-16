package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Project;
import com.example.backend.model.ProjectCategories;
import com.example.backend.model.Status;
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
    private LocalDate dateTo;
    private LocalTime timeLeft;
    private Integer userId;
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
        this.userId = project.getCreateUserId();
        this.category = project.getCategory();
    }
}
