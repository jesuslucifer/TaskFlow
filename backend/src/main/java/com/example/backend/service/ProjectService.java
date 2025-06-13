package com.example.backend.service;

import com.example.backend.exception.ProjectAlreadyExist;
import com.example.backend.exception.ProjectNotExist;
import com.example.backend.model.Project;
import com.example.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new ProjectAlreadyExist("Project already exists");
        }

        return save(project);
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotExist("Project doesn't exist");
        }
        projectRepository.deleteById(id);
    }
}
