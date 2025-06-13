package com.example.backend.controller;

import com.example.backend.dto.request.CreateProjectRequest;
import com.example.backend.dto.request.DeleteProjectRequest;
import com.example.backend.model.Project;
import com.example.backend.model.ProjectPriority;
import com.example.backend.model.ProjectStatus;
import com.example.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project/create")
    public ResponseEntity<?> create(@RequestBody CreateProjectRequest projectRequest) {
        var project = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .status(ProjectStatus.ACTIVE)
                .priority(ProjectPriority.LOW)
                .dateTo(projectRequest.getDateTo())
                .timeLeft(projectRequest.getTimeLeft())
                .createUserId(projectRequest.getUserId())
                .build();
        projectService.createProject(project);
        return ResponseEntity.ok("Project created!");
    }

    @PostMapping("/project/delete")
    public ResponseEntity<?> deleteProject(@RequestBody DeleteProjectRequest request) {
        projectService.deleteProjectById(request.getId());
        return ResponseEntity.ok("Project deleted!");
    }
}
