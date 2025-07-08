package com.example.backend.service.impl;

import com.example.backend.dto.response.TaskDto;
import com.example.backend.exception.*;
import com.example.backend.model.*;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.TaskCategoryRepository;
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
    private final TaskCategoryRepository taskCategoryRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }


    @Override
    public Task createTask(Task task, Long projectId) {
        if(!projectRepository.existsById(projectId)) {
            throw new ProjectNotExist();
        }
        if (taskRepository.existsByNameAndProjectId(
                task.getName(),
                projectId
        )) {
            throw new TaskAlreadyExistException();
        }
        task.getCategories().forEach(c -> c.setTask(task));
        Task savedTask = save(task);

        TaskList taskList = TaskList.builder()
                .projectId(projectId)
                .task(savedTask)
                .build();
        taskListRepository.save(taskList);

        return savedTask;
    }

    @Override
    public Task getById(Long id, Long projectId) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, id)){
            throw new TaskNotExistException();
        }
        return taskRepository.findById(id)
                .orElseThrow(TaskNotExistException::new);
    }

    @Override
    public Task updateById(Long projectId, Long id, TaskDto taskUpdateDto) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, id)){
            throw new TaskNotExistException();
        }

        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotExistException::new);
        task.setName(taskUpdateDto.getName());
        task.setDescription(taskUpdateDto.getDescription());
        task.setTaskStatus(taskUpdateDto.getStatus());
        task.setPriority(taskUpdateDto.getPriority());
        task.setDateTo(taskUpdateDto.getDateTo());
        task.setTimeLeft(taskUpdateDto.getTimeLeft());

        return taskRepository.save(task);
    }

    @Override
    public List<TaskDto> getAll(Long projectId) {
        if(!projectRepository.existsById(projectId)) {
            throw new ProjectNotExist();
        }
        return taskRepository.findAllByProjectId(projectId)
                .stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTaskById(Long projectId, Long id) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, id)){
            throw new TaskNotExistException();
        }
        if (!taskRepository.existsById(id)) {
            throw new TaskNotExistException();
        }
        taskRepository.deleteById(id);
    }

    @Override
    public Task addCategory(Long projectId, Long taskId, TaskCategory category) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotExistException::new);

        if (taskCategoryRepository.existsByTaskIdAndName(task.getId(), category.getName())) {
            throw new CategoryAlreadyExistsException();
        }
        category.setTask(task);

        task.addTaskCategory(category);

        return taskRepository.save(task);
    }

    @Override
    public Task deleteCategory(Long projectId, Long taskId, String categoryName) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotExistException::new);

        TaskCategory category = taskCategoryRepository.findByTaskIdAndName(taskId, categoryName)
                .orElseThrow(CategoryNotFoundException::new);

        task.removeTaskCategory(category);
        taskCategoryRepository.delete(category);

        return taskRepository.save(task);
    }
}
