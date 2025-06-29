package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "task_list")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "project_id", nullable = false)
    private long projectId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
