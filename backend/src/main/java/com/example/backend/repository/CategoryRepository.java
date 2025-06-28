package com.example.backend.repository;

import com.example.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByProjectIdAndName(Long projectId, String name);
    Boolean existsByProjectIdAndName(Long projectId, String name);
}
