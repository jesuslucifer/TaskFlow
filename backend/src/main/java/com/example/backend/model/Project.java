package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

    @Column(name = "time_left", nullable = false)
    private LocalTime timeLeft;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User createUser;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @Column(name = "date_create")
    public LocalDate dateCreate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectExecutor> executors = new ArrayList<>();

    public void addExecutor(User user, ExecutorRole role, Boolean inviteFlag) {
        executors.add(new ProjectExecutor(this, user, role, inviteFlag));
    }

    public void removeExecutor(User user) {
        executors.removeIf(executor -> executor.getUser().equals(user));
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }
}

