package com.example.backend.repository;

import com.example.backend.model.ExecutorRole;
import com.example.backend.model.ProjectExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectExecutorRepository extends JpaRepository<ProjectExecutor, Long> {
    List<ProjectExecutor> findByUserIdAndRoleNot(Long userId, ExecutorRole role);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
    ProjectExecutor findByProjectIdAndUserId(Long projectId, Long userId);
}
