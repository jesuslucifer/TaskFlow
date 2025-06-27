package com.example.backend.repository;

import com.example.backend.model.Project;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    void deleteById(@NonNull Long id);
    List<Project> findAllByCreateUserId(Long userId);
    Boolean existsByCreateUserIdAndName(Long userId, String name);
    Optional<Project> findByCreateUserIdAndName(Long userId, String name);
}
