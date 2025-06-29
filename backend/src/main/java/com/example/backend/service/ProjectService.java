package com.example.backend.service;

import com.example.backend.dto.response.ProjectDto;
import com.example.backend.model.Category;
import com.example.backend.model.ExecutorRole;
import com.example.backend.model.Project;

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

    List<ProjectDto> getAll();

    void deleteProjectById(Long id);

    List<Project> getProjectsByUserId(Long userId);

    List<ProjectDto> getProjectsByUserIdIsCreator(Long userId);

    List<ProjectDto> getProjectsByUserIsExecutor(Long userId);

    Project getProjectByNameForUsername(String username, String name);
}
