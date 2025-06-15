package com.example.backend.repository;

import com.example.backend.model.Project;
import com.example.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
    Boolean existsByName(String name);
    void deleteById(Long id);
}
