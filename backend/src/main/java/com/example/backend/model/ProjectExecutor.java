package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_executors")
public class ProjectExecutor {

    @EmbeddedId
    private ProjectExecutorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ExecutorRole role;

    public ProjectExecutor(Project project, User user, ExecutorRole executorRole) {
        this.project = project;
        this.user = user;
        this.role = executorRole;
        this.id = new ProjectExecutorId(project.getId(), user.getId());
    }
}
