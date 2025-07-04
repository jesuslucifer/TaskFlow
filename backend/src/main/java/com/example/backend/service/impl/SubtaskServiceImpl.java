package com.example.backend.service.impl;

import com.example.backend.exception.SubtaskAlreadyExistException;
import com.example.backend.exception.TaskNotExistException;
import com.example.backend.model.Subtask;
import com.example.backend.repository.SubtaskRepository;
import com.example.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public Subtask save(Subtask subtask) {
        return subtaskRepository.save(subtask);
    }

    public Subtask createSubtask(Subtask subtask, Long projectId, Long taskId) {
        if(!taskRepository.existsByTaskIdAndProjectId(projectId, taskId)) {
            throw new TaskNotExistException();
        }
        if(subtaskRepository.existByNameAndTaskId(subtask.getName(), taskId)) {
            throw new SubtaskAlreadyExistException();
        }
        return subtaskRepository.save(subtask);
    }
}
