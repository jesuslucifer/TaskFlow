package com.example.backend.service;

import com.example.backend.dto.response.ProjectDto;
import com.example.backend.model.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Project save(Project project);

    Project createProject(Project project);

    Project getById(Long id);

    Project updateById(Long id, ProjectDto updateDto);

    Project addExecutor(Long projectId, Long executorId, ExecutorRole role);

    Project deleteExecutor(Long projectId, Long executorId);

    Project addCategory(Long projectId, Category category);

    Project deleteCategory(Long projectId, String categoryName);

    List<ProjectDto> getAll(Status status, Priority priority, String name, Pageable pageable, String role, Long userId);

    void deleteProjectById(Long id);

    Project getProjectByNameForUsername(String username, String name);
}
