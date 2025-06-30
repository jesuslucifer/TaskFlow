package com.example.backend.service.impl;

import com.example.backend.dto.response.TaskDto;
import com.example.backend.exception.ProjectAlreadyExist;
import com.example.backend.exception.ProjectGetFailedException;
import com.example.backend.exception.ProjectNotExist;
import com.example.backend.model.Task;
import com.example.backend.model.TaskList;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.TaskListRepository;
import com.example.backend.repository.TaskRepository;
import com.example.backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }


    @Override
    public Task createTask(Task task, Long projectId) {
        if (taskRepository.existsByNameAndProjectId(
                task.getName(),
                projectId
        )) {
            throw new ProjectAlreadyExist();
        }
        if(!projectRepository.existsById(projectId)) {
            throw new ProjectNotExist();
        }
        Task savedTask = save(task);

        TaskList taskList = TaskList.builder()
                .projectId(projectId)
                .task(savedTask)
                .build();
        taskListRepository.save(taskList);

        return savedTask;
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(ProjectGetFailedException::new);

    }

    @Override
    public Task getByName(String name) {
        return taskRepository.findByName(name)
                .orElseThrow(ProjectGetFailedException::new);
    }

    @Override
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

    @Override
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

    @Override
    public List<TaskDto> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ProjectNotExist();
        }
        taskRepository.deleteById(id);
    }
}
