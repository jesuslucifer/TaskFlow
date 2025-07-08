package com.example.backend.service.impl;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.exception.*;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.service.ExecutorNotificationService;
import com.example.backend.service.ProjectNotificationSettingsService;
import com.example.backend.service.ProjectService;
import com.example.backend.service.UserService;
import com.example.backend.specification.ProjectSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProjectExecutorRepository projectExecutorRepository;
    private final CategoryRepository categoryRepository;
    private final ExecutorNotificationService executorNotificationService;
    private final ProjectNotificationSettingsService projectNotificationSettingsService;

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

        save(project);

        projectNotificationSettingsService.createProjectNotificationSettings(
                project,
                project.getCreateUser(),
                DeliveryMethod.EMAIL);

        projectNotificationSettingsService.createProjectNotificationSettings(
                project,
                project.getCreateUser(),
                DeliveryMethod.PUSH
        );

        return project;
    }

    @Override
    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(ProjectGetFailedException::new);
    }

    @Override
    public Project updateById(Long id, ProjectDto updateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotExist::new);

        project.setName(updateDto.getName());
        project.setDescription(updateDto.getDescription());
        if (!project.getStatus().equals(updateDto.getStatus())) {
            project.setStatus(updateDto.getStatus());
            executorNotificationService.sendNotificationToChangeStatusProject(project);
        }
        project.setPriority(updateDto.getPriority());
        project.setDateTo(updateDto.getDateTo());
        project.setTimeLeft(updateDto.getTimeLeft());

        return projectRepository.save(project);
    }

    @Override
    public Project addExecutor(Long projectId, Long executorId, ExecutorRole role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        if (projectExecutorRepository.existsByProjectIdAndUserId(project.getId(), user.getId())) {
            throw new ExecutorAlreadyExistsInProjectException();
        }

        project.addExecutor(user, role, false);

        projectNotificationSettingsService.createProjectNotificationSettings(
                project,
                user,
                DeliveryMethod.EMAIL);

        projectNotificationSettingsService.createProjectNotificationSettings(
                project,
                user,
                DeliveryMethod.PUSH
        );

        executorNotificationService.sendNotificationToAddExecutor(user, project);

        return projectRepository.save(project);
    }

    @Override
    public Project deleteExecutor(Long projectId, Long executorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        if (!projectExecutorRepository.existsByProjectIdAndUserId(project.getId(), user.getId())) {
            throw new ExecutorNotFoundInProjectException();
        }

        executorNotificationService.sendNotificationToDeleteExecutor(user, project);

        project.removeExecutor(user);

        return projectRepository.save(project);
    }

    @Override
    public Project acceptExecutor(Long projectId, Long executorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        if (!projectExecutorRepository.existsByProjectIdAndUserId(project.getId(), user.getId())) {
            throw new ExecutorNotFoundInProjectException();
        }

        projectExecutorRepository.findByProjectIdAndUserId(project.getId(), user.getId()).setInviteFlag(true);

        executorNotificationService.sendNotificationToAcceptInviteExecutor(project.getCreateUser(), project, user);

        return projectRepository.save(project);
    }

    @Override
    public Project declineExecutor(Long projectId, Long executorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotExist::new);

        User user = userRepository.findById(executorId)
                .orElseThrow(UserNotFoundException::new);

        if (!projectExecutorRepository.existsByProjectIdAndUserId(project.getId(), user.getId())) {
            throw new ExecutorNotFoundInProjectException();
        }

        project.removeExecutor(user);

        executorNotificationService.sendNotificationToDeclineInviteExecutor(project.getCreateUser(), project, user);

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
    public List<ProjectDto> getAll(Status status, Priority priority, String name, Pageable pageable, String role, Long userId) {
        Specification<Project> spec = Specification
                .where(ProjectSpecification.statusEquals(status))
                .and(ProjectSpecification.priorityEquals(priority))
                .and(ProjectSpecification.nameLike(name));

        if ("creator".equals(role)) {
            spec = spec.and(ProjectSpecification.isCreator(userId));
        } else if ("executor".equals(role)) {
            spec = spec.and(ProjectSpecification.isExecutor(userId));
        }

        return projectRepository.findAll(spec, pageable)
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
    public Project getProjectByNameForUsername(String username, String name) {
        return projectRepository.findByCreateUserIdAndName(userService.getByUsername(username).getId(), name)
                .orElseThrow(ProjectNotExist::new);
    }
}
