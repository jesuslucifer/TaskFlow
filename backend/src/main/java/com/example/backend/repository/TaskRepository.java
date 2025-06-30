package com.example.backend.repository;

import com.example.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
    void deleteById(Long id);
    @Query("""
        SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
        FROM Task t
        JOIN t.taskLists tl
        WHERE t.createUser.id = :userId
          AND t.name = :name
          AND tl.projectId = :projectId
    """)
    boolean existsByCreateUserIdAndNameAndProjectId(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("projectId") Long projectId
    );
}
