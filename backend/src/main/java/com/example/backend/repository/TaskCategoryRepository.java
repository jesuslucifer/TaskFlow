package com.example.backend.repository;

import com.example.backend.model.Task;
import com.example.backend.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
    Optional<TaskCategory> findByTaskIdAndName(Long taskId, String name);
    Boolean existsByTaskIdAndName(Long taskId, String name);

    Long task(Task task);
}
