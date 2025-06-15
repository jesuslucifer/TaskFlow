package com.example.backend.repository;

import com.example.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    Boolean existsByName(String name);
    void deleteById(Long id);
}
