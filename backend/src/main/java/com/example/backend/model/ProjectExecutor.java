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
    private UserProjectId id;

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

    @Column(name = "invite_flag")
    private Boolean inviteFlag;

    public ProjectExecutor(Project project, User user, ExecutorRole executorRole, Boolean inviteFlag) {
        this.project = project;
        this.user = user;
        this.role = executorRole;
        this.id = new UserProjectId(project.getId(), user.getId());
        this.inviteFlag = inviteFlag;
    }
}
