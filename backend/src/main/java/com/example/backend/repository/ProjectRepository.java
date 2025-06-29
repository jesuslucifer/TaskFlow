package com.example.backend.repository;

import com.example.backend.model.Project;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    void deleteById(@NonNull Long id);
    List<Project> findAllByCreateUserId(Long userId);
    Boolean existsByCreateUserIdAndName(Long userId, String name);
    Optional<Project> findByCreateUserIdAndName(Long userId, String name);

    @EntityGraph(attributePaths = {"executors", "createUser"})
    List<Project> findAllByDateToBetween(LocalDate dateToAfter, LocalDate dateToBefore);
}
