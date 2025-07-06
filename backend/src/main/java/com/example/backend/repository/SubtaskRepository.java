package com.example.backend.repository;

import com.example.backend.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    @Query("""
    SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
    FROM Subtask s
    WHERE s.name = :name
      AND s.taskId = :taskId
""")
    Boolean existByNameAndTaskId(String name, Long taskId);

    @Query("""
    SELECT s
    FROM Subtask s 
    WHERE s.taskId = :taskId
""")
    List<Subtask> findAllByTaskId(Long taskId);
}
