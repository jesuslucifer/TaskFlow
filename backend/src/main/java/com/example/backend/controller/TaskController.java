package com.example.backend.controller;

import com.example.backend.dto.request.CreateTaskRequest;
import com.example.backend.dto.response.TaskDto;
import com.example.backend.model.*;
import com.example.backend.service.TaskService;
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

    @PostMapping("/{projectId}/task/create")
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

    @GetMapping("/get/id/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return ResponseEntity.ok(Map.of
                ("task_name", task.getName() ,
                        "description", task.getDescription(),
                        "status", task.getStatus(),
                        "priority", task.getPriority(),
                        //"date_to", task.getDateTo(),
                        "time_left", task.getTimeLeft()
                ));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<?> getTaskByName(@PathVariable String name) {
        Task task = taskService.getByName(name);
        return ResponseEntity.ok(Map.of
                ("task_name", task.getName() ,
                        "description", task.getDescription(),
                        "status", task.getStatus(),
                        "priority", task.getPriority(),
                        //"date_to", task.getDateTo(),
                        "time_left", task.getTimeLeft()
                ));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getTasks() {
        List<TaskDto> taskDto = taskService.getAll();

        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<Task> updateTaskByid(
            @PathVariable Long id,
            @RequestBody TaskDto updateDto) {
        Task updatedTask = taskService.updateById(id, updateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<Task> updateTaskByName(
            @PathVariable String name,
            @RequestBody TaskDto updateDto) {
        Task updatedTask = taskService.updateByName(name, updateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task deleted!");
    }
}
