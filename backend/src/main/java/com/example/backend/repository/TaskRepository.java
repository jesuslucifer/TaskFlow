package com.example.backend.repository;

import com.example.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
    void deleteById(Long id);

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM Task t
    JOIN t.taskLists tl
    WHERE t.name = :name
      AND tl.projectId = :projectId
""")
    boolean existsByNameAndProjectId(
            @Param("name") String name,
            @Param("projectId") Long projectId
    );

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ElSE false END
    FROM Task t 
    JOIN t.taskLists tl
    WHERE t.id = :taskId 
      AND tl.projectId = :projectId
""")
    boolean existsByTaskIdAndProjectId(
            @Param("projectId") Long projectId,
            @Param("taskId") Long taskId
    );

    @Query("""
    SELECT t
    FROM Task t 
    JOIN t.taskLists tl
    WHERE tl.projectId = :projectId
""")
    List<Task> findAllByProjectId(
            @Param("projectId") Long projectId
    );
}
