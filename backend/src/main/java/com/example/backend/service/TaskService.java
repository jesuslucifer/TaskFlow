package com.example.backend.service;

import com.example.backend.dto.response.TaskDto;
import com.example.backend.model.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task);

    //Task createTask(Task task);

    public Task createTask(Task task, Long projectId);

    Task getById(Long id, Long projectId);

    Task updateById(Long projectId, Long id, TaskDto taskUpdateDto);

    List<TaskDto> getAll(Long projectId);

    void deleteTaskById(Long projectId, Long id);
}
