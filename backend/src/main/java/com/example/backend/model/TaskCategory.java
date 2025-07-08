package com.example.backend.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task_categories")
public class TaskCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Hidden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "name")
    private String name;

    public TaskCategory() {}
}
