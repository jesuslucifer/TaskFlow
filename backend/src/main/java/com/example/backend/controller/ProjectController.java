package com.example.backend.controller;

import com.example.backend.dto.request.CreateProjectRequest;
import com.example.backend.dto.request.ProjectExecutorRequest;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.model.*;
import com.example.backend.service.ProjectService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

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
                .createUser(user)
                .dateCreate(LocalDate.now())
                .categories(projectRequest.getCategories())
                .executors(new ArrayList<>())
                .build();

        project.addExecutor(userService.getById(user.getId()), ExecutorRole.ADMINISTRATOR);

        projectService.createProject(project);

        return ResponseEntity.ok(new ProjectDto(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectByID(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return ResponseEntity.ok(new ProjectDto(project));
    }

    @GetMapping("/{username}/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name,
                                              @PathVariable String username) {
        Project project = projectService.getProjectByNameForUsername(username, name);

        return ResponseEntity.ok(new ProjectDto(project));
    }

    @GetMapping()
    public ResponseEntity<?> getProjects(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "creator") String role,
            Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(projectService
                .getAll(status, priority, name, pageable, role, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProjectById(
            @PathVariable Long id,
            @RequestBody ProjectDto updateDto) {

        projectService.updateById(id, updateDto);

        return ResponseEntity.ok(new ProjectDto(projectService.getById(id)));
    }

    @PutMapping("/{projectId}/executors/")
    public ResponseEntity<?> addExecutor(@PathVariable Long projectId,
                                         @RequestBody ProjectExecutorRequest executorRequest) {
        projectService.addExecutor(projectId,
                executorRequest.getExecutorId(),
                executorRequest.getExecutorRole());

        return ResponseEntity.ok(new ProjectDto(projectService.getById(projectId)));
    }

    @PutMapping("/{projectId}/category/")
    public ResponseEntity<?> addCategory(@PathVariable Long projectId,
                                         @RequestBody Category category) {
        projectService.addCategory(projectId, category);

        return ResponseEntity.ok(new ProjectDto(projectService.getById(projectId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProjectById(id);

        return ResponseEntity.ok(new SuccessResponse(
                "Проект удален",
                HttpStatus.OK
        ));
    }

    @DeleteMapping("/{projectId}/{executorId}/executors")
    public ResponseEntity<?> deleteExecutor(@PathVariable Long projectId,
                                            @PathVariable Long executorId) {
        projectService.deleteExecutor(projectId, executorId);

        return ResponseEntity.ok(new SuccessResponse(
                "Исполнитель удален",
                HttpStatus.OK
        ));
    }

    @DeleteMapping("/{projectId}/{categoryName}/category")
    public ResponseEntity<?> deleteCategory(@PathVariable Long projectId,
                                            @PathVariable String categoryName) {
        projectService.deleteCategory(projectId, categoryName);

        return ResponseEntity.ok(new SuccessResponse(
                "Категория удалена",
                HttpStatus.OK
        ));
    }
}
