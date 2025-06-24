package com.example.backend.service;

import com.example.backend.dto.response.TaskDto;
import com.example.backend.model.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task);

    Task createTask(Task task);

    Task getById(Long id);

    Task getByName(String name);

    Task updateById(Long id, TaskDto taskUpdateDto);

    Task updateByName(String name, TaskDto taskUpdateDto);

    List<TaskDto> getAll();

    void deleteTaskById(Long id);
}
