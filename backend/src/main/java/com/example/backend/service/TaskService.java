package com.example.backend.service;

import com.example.backend.dto.response.TaskDto;
import com.example.backend.exception.ProjectAlreadyExist;
import com.example.backend.exception.ProjectGetFailedException;
import com.example.backend.exception.ProjectNotExist;
import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task createTask(Task task) {
        if(taskRepository.existsByName(task.getName())) {
            throw new ProjectAlreadyExist();
        }

        return save(task);
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(ProjectGetFailedException::new);

    }

    public Task getByName(String name) {
        return taskRepository.findByName(name)
                .orElseThrow(ProjectGetFailedException::new);
    }

    public Task updateById(Long id, TaskDto taskUpdateDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(ProjectNotExist::new);
        task.setName(taskUpdateDto.getName());
        task.setDescription(taskUpdateDto.getDescription());
        task.setStatus(taskUpdateDto.getStatus());
        task.setPriority(taskUpdateDto.getPriority());
        task.setDateTo(taskUpdateDto.getDateTo());
        task.setTimeLeft(taskUpdateDto.getTimeLeft());
        return taskRepository.save(task);
    }

    public Task updateByName(String name, TaskDto taskUpdateDto) {
        Task task = taskRepository.findByName(name)
                .orElseThrow(ProjectNotExist::new);
        task.setName(taskUpdateDto.getName());
        task.setDescription(taskUpdateDto.getDescription());
        task.setStatus(taskUpdateDto.getStatus());
        task.setPriority(taskUpdateDto.getPriority());
        task.setDateTo(taskUpdateDto.getDateTo());
        task.setTimeLeft(taskUpdateDto.getTimeLeft());
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ProjectNotExist();
        }
        taskRepository.deleteById(id);
    }
}
