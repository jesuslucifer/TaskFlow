package com.example.backend.repository;

import com.example.backend.model.Project;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    void deleteById(@NonNull Long id);
    Boolean existsByCreateUserIdAndName(Long userId, String name);
    Optional<Project> findByCreateUserIdAndName(Long userId, String name);
    List<Project> findAllByDateToIs(LocalDate dateTo);
    List<Project> findAllByDateToAndTimeLeftBetween(LocalDate dateTo, LocalTime timeLeftAfter, LocalTime timeLeftBefore);
}
