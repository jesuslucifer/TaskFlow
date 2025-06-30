package com.example.backend.repository;

import com.example.backend.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    boolean existsByProjectIdAndTask_NameAndTask_CreateUserId(Long projectId, String taskName, Long userId);
}
