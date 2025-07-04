package com.example.backend.controller;

import com.example.backend.dto.request.CreateSubtaskRequest;
import com.example.backend.dto.request.CreateTaskRequest;
import com.example.backend.dto.response.TaskDto;
import com.example.backend.dto.response.TaskDtoWithId;
import com.example.backend.model.*;
import com.example.backend.service.TaskService;
import com.example.backend.service.impl.SubtaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
                .status(Status.ACTIVE)
                .priority(Priority.LOW)
                .dateTo(taskRequest.getDateTo())
                .timeLeft(taskRequest.getTimeLeft())
                .createUser(user)
                .build();

        taskService.createTask(task, projectId);

        return ResponseEntity.ok("Task created!");
    }

    @GetMapping("{projectId}/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, @PathVariable Long projectId) {
        Task task = taskService.getById(id, projectId);
        return ResponseEntity.ok(Map.of
                ("name", task.getName() ,
                        "description", task.getDescription(),
                        "status", task.getStatus(),
                        "priority", task.getPriority(),
                        //"date_to", task.getDateTo(),
                        "time_left", task.getTimeLeft()
                ));
    }

    @GetMapping("{projectId}/tasks/all")
    public ResponseEntity<?> getTasks(@PathVariable Long projectId) {
        List<TaskDtoWithId> taskDto = taskService.getAll(projectId);

        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("{projectId}/tasks/{id}/update")
    public ResponseEntity<TaskDto> updateTaskByid(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @RequestBody TaskDto updateDto) {
        Task updatedTask = taskService.updateById(projectId, id, updateDto);
        TaskDto taskDtoResponse = TaskDto.builder()
                .name(updatedTask.getName())
                .description(updatedTask.getDescription())
                .status(updatedTask.getStatus())
                .priority(updatedTask.getPriority())
                .dateTo(updatedTask.getDateTo())
                .timeLeft(updatedTask.getTimeLeft())
                .build();
        return ResponseEntity.ok(taskDtoResponse);
    }

    @DeleteMapping("{projectId}/tasks/{id}/delete")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        taskService.deleteTaskById(projectId, id);
        return ResponseEntity.ok("Task deleted!");
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
                .status(Status.ACTIVE)
                .priority(Priority.LOW)
                .createUser(user)
                .taskId(id)
                .build();
        subtaskService.createSubtask(subtask, projectId, id);
        return ResponseEntity.ok("Task subtask created!");
    }
}
