package com.example.backend.repository;

import com.example.backend.model.ProjectExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectExecutorRepository extends JpaRepository<ProjectExecutor, Long> {
    @EntityGraph(attributePaths = "project")
    List<ProjectExecutor> findByUserId(Long userId);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
