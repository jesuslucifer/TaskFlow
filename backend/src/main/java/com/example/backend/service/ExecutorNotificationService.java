package com.example.backend.service;

import com.example.backend.model.Project;
import com.example.backend.model.User;

public interface ExecutorNotificationService {
    void sendNotificationToAddExecutor(User user, Project project);
    void sendNotificationToDeleteExecutor(User user, Project project);
    void sendNotificationToAcceptInviteExecutor(User creator, Project project, User executor);
    void sendNotificationToDeclineInviteExecutor(User creator, Project project, User executor);
    void sendNotificationToChangeStatusProject(Project project);
}
