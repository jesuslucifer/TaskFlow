package com.example.backend.service;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.exception.ProjectAlreadyExist;
import com.example.backend.exception.ProjectGetFailedException;
import com.example.backend.exception.ProjectNotExist;
import com.example.backend.model.Project;
import com.example.backend.model.User;
import com.example.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

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

    public Project updateById(Long id, ProjectDto projectUpdateDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotExist::new);
        project.setName(projectUpdateDto.getName());
        project.setDescription(projectUpdateDto.getDescription());
        project.setStatus(projectUpdateDto.getStatus());
        project.setPriority(projectUpdateDto.getPriority());
        project.setDateTo(projectUpdateDto.getDateTo());
        project.setTimeLeft(projectUpdateDto.getTimeLeft());
        project.setCreateUser(project.getCreateUser());
        project.setCategory(project.getCategory());

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

    public Project getProjectByNameForUsername(String username, String name) {
        return getProjectsByUserId(userService.getByUsername(username).getId())
                .stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(ProjectNotExist::new);
    }
}
