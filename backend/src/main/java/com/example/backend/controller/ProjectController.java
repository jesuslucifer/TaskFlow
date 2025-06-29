package com.example.backend.controller;

import com.example.backend.dto.request.CreateProjectRequest;
import com.example.backend.dto.request.ProjectExecutorRequest;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.model.*;
import com.example.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
                .createUser(user)
                .dateCreate(LocalDate.now())
                .categories(projectRequest.getCategories())
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
        return ResponseEntity.ok(new ProjectDto(project));
    }

    @GetMapping("/{username}/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name,
                                              @PathVariable String username) {
        Project project = projectService.getProjectByNameForUsername(username, name);

        return ResponseEntity.ok(new ProjectDto(project));
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

    @GetMapping("/{id}/executor")
    public ResponseEntity<?> getProjectsUserIsExecutor(@PathVariable Long id) {
        List<ProjectDto> projectDtoList = projectService.getProjectsByUserIsExecutor(id);

        return ResponseEntity.ok(projectDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProjectById(
            @PathVariable Long id,
            @RequestBody ProjectDto updateDto) {

        projectService.updateById(id, updateDto);

        return ResponseEntity.ok(new SuccessResponse(
                "Проект обновлен",
                HttpStatus.OK
        ));
    }

    @PutMapping("/{projectId}/executors/")
    public ResponseEntity<?> addExecutor(@PathVariable Long projectId,
                                         @RequestBody ProjectExecutorRequest executorRequest) {
        projectService.addExecutor(projectId,
                executorRequest.getExecutorId(),
                executorRequest.getExecutorRole());

        return ResponseEntity.ok(new SuccessResponse(
                "Исполнитель добавлен",
                HttpStatus.OK
        ));
    }

    @PutMapping("/{projectId}/category/")
    public ResponseEntity<?> addCategory(@PathVariable Long projectId,
                                         @RequestBody Category category) {
        projectService.addCategory(projectId, category);

        return ResponseEntity.ok(new SuccessResponse(
                "Категория добавлена",
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
