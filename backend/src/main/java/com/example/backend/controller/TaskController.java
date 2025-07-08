package com.example.backend.controller;

import com.example.backend.dto.request.CreateSubtaskRequest;
import com.example.backend.dto.request.CreateTaskRequest;
import com.example.backend.dto.response.SubtaskDto;
import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.dto.response.TaskDto;
import com.example.backend.model.*;
import com.example.backend.service.TaskService;
import com.example.backend.service.impl.SubtaskServiceImpl;
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
public class TaskController {

    private final TaskService taskService;
    private final SubtaskServiceImpl subtaskService;

    @PostMapping("/{projectId}/tasks/create")
    public ResponseEntity<?> createTask(@PathVariable Long projectId, @RequestBody CreateTaskRequest taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Task task = Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .taskStatus(TaskStatus.TODO)
                .priority(Priority.LOW)
                .dateTo(taskRequest.getDateTo())
                .timeLeft(taskRequest.getTimeLeft())
                .createUser(user)
                .dateCreate(LocalDate.now())
                .build();

        taskService.createTask(task, projectId);

        return ResponseEntity.ok(new TaskDto(task));
    }

    @GetMapping("{projectId}/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, @PathVariable Long projectId) {
        Task task = taskService.getById(id, projectId);
        return ResponseEntity.ok(new TaskDto(task));
    }

    @GetMapping("{projectId}/tasks/all")
    public ResponseEntity<?> getTasks(@PathVariable Long projectId) {
        List<TaskDto> taskDto = taskService.getAll(projectId);

        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("{projectId}/tasks/{id}/update")
    public ResponseEntity<TaskDto> updateTaskByid(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @RequestBody TaskDto updateDto) {
        taskService.updateById(projectId, id, updateDto);

        return ResponseEntity.ok(new TaskDto(taskService.getById(id, projectId)));
    }

    @DeleteMapping("{projectId}/tasks/{id}/delete")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        taskService.deleteTaskById(projectId, id);
        return ResponseEntity.ok(new SuccessResponse(
                "Задача удалена",
                HttpStatus.OK
        ));
    }

    @PostMapping("{projectId}/tasks/{id}/subtasks/create")
    public ResponseEntity<?> createTaskSubtask(
            @RequestBody CreateSubtaskRequest subtaskRequest,
            @PathVariable Long projectId,
            @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Subtask subtask = Subtask.builder()
                .name(subtaskRequest.getName())
                .description(subtaskRequest.getDescription())
                .isDone(false)
                .createUser(user)
                .taskId(id)
                .build();
        subtaskService.createSubtask(subtask, projectId, id);
        return ResponseEntity.ok(new SubtaskDto(subtask));
    }

    @DeleteMapping("{projectId}/tasks/{taskId}/subtasks/{subtaskId}/delete")
    public ResponseEntity<?> deleteTaskSubtask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long subtaskId) {
        subtaskService.deleteSubtaskById(projectId, taskId, subtaskId);
        return ResponseEntity.ok(new SuccessResponse(
                "Позадача удалена",
                HttpStatus.OK
        ));
    }

    @GetMapping("{projectId}/tasks/{taskId}/subtasks/all")
    public ResponseEntity<?> getSubtasks(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        List<SubtaskDto> subtaskDto = subtaskService.getAll(projectId, taskId);

        return ResponseEntity.ok(subtaskDto);
    }

    @PutMapping("{projectId}/tasks/{taskId}/subtasks/{subtaskId}/update")
    public ResponseEntity<?> updateSubtaskById(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            @RequestBody SubtaskDto updateDto
    ) {
        subtaskService.updateSubtask(projectId, taskId, subtaskId, updateDto);

        return ResponseEntity.ok(new SubtaskDto(subtaskService.getById(projectId, taskId, subtaskId)));
    }
}
