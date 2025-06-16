package com.example.backend.service;
import com.example.backend.dto.response.ProjectDto;
import com.example.backend.dto.response.TaskDto;
import com.example.backend.dto.response.UserDto;
import com.example.backend.exception.ProjectAlreadyExist;
import com.example.backend.exception.ProjectGetFailedException;
import com.example.backend.exception.ProjectNotExist;
import com.example.backend.model.Project;
import com.example.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    @Autowired
    private final ProjectRepository projectRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project createProject(Project project) {
        if(projectRepository.existsByName(project.getName())) {
            throw new ProjectAlreadyExist();
        }

        return save(project);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(ProjectGetFailedException::new);

    }

    public Project getByName(String name) {
        return projectRepository.findByName(name)
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
        project.setCreateUserId(projectUpdateDto.getUserId());
        project.setCategory(projectUpdateDto.getCategory());
        return projectRepository.save(project);
    }

    public Project updateByName(String name, ProjectDto projectUpdateDto) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(ProjectNotExist::new);
        project.setName(projectUpdateDto.getName());
        project.setDescription(projectUpdateDto.getDescription());
        project.setStatus(projectUpdateDto.getStatus());
        project.setPriority(project.getPriority());
        project.setDateTo(projectUpdateDto.getDateTo());
        project.setTimeLeft(projectUpdateDto.getTimeLeft());
        project.setCreateUserId(project.getCreateUserId());
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
}
