package com.example.backend.controller;

import com.example.backend.dto.request.CreateProjectRequest;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.dto.response.UserDto;
import com.example.backend.model.*;
import com.example.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        var project = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .status(Status.ACTIVE)
                .priority(projectRequest.getPriority())
                .dateTo(projectRequest.getDateTo())
                .timeLeft(projectRequest.getTimeLeft())
                .category(ProjectCategories.IOS_APP)
                .createUser(user)
                .build();

        projectService.createProject(project);

        return ResponseEntity.ok(new SuccessResponse(
                "Проект создан",
                HttpStatus.OK
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectByID(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return ResponseEntity.ok(Map.of
                ("project_name", project.getName() ,
                        "description", project.getDescription(),
                        "status", project.getStatus(),
                        "priority", project.getPriority(),
                        "date_to", project.getDateTo(),
                        "time_left", project.getTimeLeft(),
                        "create_user_id", project.getCreateUser().getId(),
                        "category", project.getCategory()
                        ));
    }

    @GetMapping("/{username}/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name,
                                              @PathVariable String username) {
        Project project = projectService.getProjectByNameForUsername(username, name);

        return ResponseEntity.ok(Map.of
                ("project_name", project.getName() ,
                        "description", project.getDescription(),
                        "status", project.getStatus(),
                        "priority", project.getPriority(),
                        "date_to", project.getDateTo(),
                        "time_left", project.getTimeLeft(),
                        "create_user_id", project.getCreateUser().getId(),
                        "category", project.getCategory()
                ));
    }

    @GetMapping("/")
    public ResponseEntity<?> getProjects() {
        List<ProjectDto> projectDto = projectService.getAll();

        return ResponseEntity.ok(projectDto);
    }

    @GetMapping("/{id}/creator")
    public ResponseEntity<?> getProjectsUserIsCreator(@PathVariable Long id) {
        List<ProjectDto> projectDtoList = projectService.getProjectsByUserIdIsCreator(id);

        return ResponseEntity.ok(projectDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProjectById(
            @PathVariable Long id,
            @RequestBody ProjectDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        updateDto.setCreateUser(new UserDto(user));
        projectService.updateById(id, updateDto);

        return ResponseEntity.ok(new SuccessResponse(
                "Проект обновлен",
                HttpStatus.OK
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProjectById(id);

        return ResponseEntity.ok(new SuccessResponse(
                "Проект удален",
                HttpStatus.OK
        ));
    }
}
