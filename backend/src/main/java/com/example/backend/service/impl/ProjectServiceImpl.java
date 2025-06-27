package com.example.backend.service.impl;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.exception.*;
import com.example.backend.model.*;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProjectExecutorRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ProjectService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProjectExecutorRepository projectExecutorRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project createProject(Project project) {

        if (projectRepository.existsByCreateUserIdAndName(project.getCreateUser().getId(), project.getName())) {
            throw new ProjectAlreadyExist();
        }

        project.getCategories().forEach(c -> c.setProject(project));
        return save(project);
    }

    @Override
    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(ProjectGetFailedException::new);
    }

    @Override
    public Project updateById(Long id, Map<String, Object> updateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotExist::new);

        updateDto.forEach((k, v) -> {
            switch (k) {
                case "name":
                    project.setName(v.toString());
                    break;
                case "description":
                    project.setDescription(v.toString());
                    break;
                case "status":
                    project.setStatus(Status.valueOf(v.toString()));
                    break;
                case "priority":
                    project.setPriority(Priority.valueOf(v.toString()));
                    break;
                case "dateTo":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    project.setDateTo(LocalDate.parse(v.toString(), formatter));
                    break;
                case "timeLeft":
                    project.setTimeLeft(LocalTime.parse(v.toString()));
                    break;
                default:
                    break;
            }
        });

        return projectRepository.save(project);
    }

    @Override
    public Project addExecutor(Long projectId, Long executorId, ExecutorRole role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        project.addExecutor(user, role);

        return projectRepository.save(project);
    }

    @Override
    public Project deleteExecutor(Long projectId, Long executorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        project.removeExecutor(user);

        return projectRepository.save(project);
    }

    @Override
    public Project addCategory(Long projectId, Category category) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        if (categoryRepository.existsByProjectIdAndName(project.getId(), category.getName())) {
            throw new CategoryAlreadyExistsException();
        }

        category.setProject(project);

        project.addCategory(category);

        return projectRepository.save(project);
    }

    @Override
    public Project deleteCategory(Long projectId, String categoryName) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        Category category = categoryRepository.findByProjectIdAndName(projectId, categoryName)
                .orElseThrow(CategoryNotFoundException::new);

        project.removeCategory(category);
        categoryRepository.delete(category);

        return projectRepository.save(project);
    }

    @Override
    public List<ProjectDto> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotExist();
        }
        projectRepository.deleteById(id);
    }

    @Override
    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findAllByCreateUserId(userId);
    }

    @Override
    public List<ProjectDto> getProjectsByUserIdIsCreator(Long userId) {
        return projectRepository
                .findAllByCreateUserId(userId)
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getProjectsByUserIsExecutor(Long userId) {
        return projectExecutorRepository.findByUserId(userId)
                .stream()
                .map(ProjectExecutor::getProject)
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Project getProjectByNameForUsername(String username, String name) {
        return projectRepository.findByCreateUserIdAndName(userService.getByUsername(username).getId(), name)
                .orElseThrow(ProjectNotExist::new);
    }
}
