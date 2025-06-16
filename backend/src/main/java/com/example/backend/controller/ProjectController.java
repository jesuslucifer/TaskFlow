package com.example.backend.controller;

import com.example.backend.dto.request.CreateProjectRequest;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.model.*;
import com.example.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateProjectRequest projectRequest) {
        var project = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .status(Status.ACTIVE)
                .priority(Priority.LOW)
                .dateTo(projectRequest.getDateTo())
                .timeLeft(projectRequest.getTimeLeft())
                .createUserId(projectRequest.getUserId())
                .category(ProjectCategories.IOS_APP)
                .build();
        projectService.createProject(project);
        return ResponseEntity.ok("Project created!");
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<?> getProjectByID(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return ResponseEntity.ok(Map.of
                ("project_name", project.getName() ,
                        "description", project.getDescription(),
                        "status", project.getStatus(),
                        "priority", project.getPriority(),
                        "date_to", project.getDateTo(),
                        "time_left", project.getTimeLeft(),
                        "create_user_id", project.getCreateUserId(),
                        "category", project.getCategory()
                        ));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        Project project = projectService.getByName(name);
        return ResponseEntity.ok(Map.of
                ("project_name", project.getName() ,
                        "description", project.getDescription(),
                        "status", project.getStatus(),
                        "priority", project.getPriority(),
                        "date_to", project.getDateTo(),
                        "time_left", project.getTimeLeft(),
                        "create_user_id", project.getCreateUserId(),
                        "category", project.getCategory()
                ));
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<Project> updateProjectByid(
            @PathVariable Long id,
            @RequestBody ProjectDto updateDto) {
        Project updatedProject = projectService.updateById(id, updateDto);
        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<Project> updateProjectByName(
            @PathVariable String name,
            @RequestBody ProjectDto updateDto){
        Project updatedProject = projectService.updateByName(name, updateDto);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProjects() {
        List<ProjectDto> projectDto = projectService.getAll();

        return ResponseEntity.ok(projectDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok("Project deleted!");
    }
}
