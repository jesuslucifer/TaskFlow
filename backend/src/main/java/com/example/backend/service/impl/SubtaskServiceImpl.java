package com.example.backend.service.impl;

import com.example.backend.dto.response.SubtaskDto;
import com.example.backend.exception.SubtaskAlreadyExistException;
import com.example.backend.exception.TaskNotExistException;
import com.example.backend.model.Subtask;
import com.example.backend.repository.SubtaskRepository;
import com.example.backend.repository.TaskRepository;
import com.example.backend.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    @Override
    public Subtask save(Subtask subtask) {
        return subtaskRepository.save(subtask);
    }

    @Override
    public Subtask createSubtask(Subtask subtask, Long projectId, Long taskId) {
        if(!taskRepository.existsByTaskIdAndProjectId(projectId, taskId)) {
            throw new TaskNotExistException();
        }
        if(subtaskRepository.existByNameAndTaskId(subtask.getName(), taskId)) {
            throw new SubtaskAlreadyExistException();
        }
        return subtaskRepository.save(subtask);
    }

    @Override
    public void deleteSubtaskById(Long projectId, Long taskId, Long subtaskId) {
        if(!taskRepository.existsByTaskIdAndProjectId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        subtaskRepository.deleteById(subtaskId);
    }

    @Override
    public List<SubtaskDto> getAll(Long projectId, Long taskId) {
        if(!taskRepository.existsByTaskIdAndProjectId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        return subtaskRepository.findAllByTaskId(taskId)
                .stream()
                .map(subtask -> new SubtaskDto(
                        subtask.getName(),
                        subtask.getDescription(),
                        subtask.getStatus(),
                        subtask.getPriority()
                ))
                .collect(Collectors.toList());
    }
}
