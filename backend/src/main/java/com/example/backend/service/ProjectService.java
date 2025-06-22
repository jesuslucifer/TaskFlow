package com.example.backend.service;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.exception.ProjectAlreadyExist;
import com.example.backend.exception.ProjectGetFailedException;
import com.example.backend.exception.ProjectNotExist;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.ProjectExecutorRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.UserRepository;
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
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProjectExecutorRepository projectExecutorRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project createProject(Project project) {
        getProjectsByUserId(project.getCreateUser().getId())
                .stream()
                .filter(p -> p.getName().equals(project.getName()))
                .findFirst()
                .ifPresent(p -> { throw new ProjectAlreadyExist(); });
        return save(project);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(ProjectGetFailedException::new);

    }

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
                case "category":
                    project.setCategory(ProjectCategories.valueOf(v.toString()));
                    break;
                default:
                    break;
            }
        });

        return projectRepository.save(project);
    }

    public Project addExecutor(Long projectId, Long executorId, ExecutorRole role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        project.addExecutor(user, role);

        return projectRepository.save(project);
    }

    public Project deleteExecutor(Long projectId, Long executorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        project.removeExecutor(user);

        return projectRepository.save(project);
    }

    public List<ProjectDto> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotExist();
        }
        projectRepository.deleteById(id);
    }

    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findAllByCreateUserId(userId);
    }

    public List<ProjectDto> getProjectsByUserIdIsCreator(Long userId) {
        return projectRepository
                .findAllByCreateUserId(userId)
                .stream()
                .filter(p -> p.getCreateUser().getId().equals(userId))
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    public List<ProjectDto> getProjectsByUserIsExecutor(Long userId) {
        return projectExecutorRepository.findByUserId(userId)
                .stream()
                .map(ProjectExecutor::getProject)
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    public Project getProjectByNameForUsername(String username, String name) {
        return getProjectsByUserId(userService.getByUsername(username).getId())
                .stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(ProjectNotExist::new);
    }
}
