package com.example.backend.service.impl;

import com.example.backend.dto.response.SubtaskDto;
import com.example.backend.exception.SubtaskAlreadyExistException;
import com.example.backend.exception.SubtaskNotExistException;
import com.example.backend.exception.TaskNotExistException;
import com.example.backend.model.Subtask;
import com.example.backend.repository.SubtaskRepository;
import com.example.backend.repository.TaskListRepository;
import com.example.backend.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskListRepository taskListRepository;

    @Override
    public Subtask save(Subtask subtask) {
        return subtaskRepository.save(subtask);
    }

    @Override
    public Subtask createSubtask(Subtask subtask, Long projectId, Long taskId) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)) {
            throw new TaskNotExistException();
        }
        if(subtaskRepository.existByNameAndTaskId(subtask.getName(), taskId)) {
            throw new SubtaskAlreadyExistException();
        }
        return subtaskRepository.save(subtask);
    }

    @Override
    public void deleteSubtaskById(Long projectId, Long taskId, Long subtaskId) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        subtaskRepository.deleteById(subtaskId);
    }

    @Override
    public List<SubtaskDto> getAll(Long projectId, Long taskId) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        return subtaskRepository.findAllByTaskId(taskId)
                .stream()
                .map(SubtaskDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Subtask getById(Long projectId, Long taskId, Long subtaskId) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)){
            throw new TaskNotExistException();
        }
        return subtaskRepository.findById(subtaskId)
                .orElseThrow(SubtaskNotExistException::new);
    }

    @Override
    public void updateSubtask(Long projectId, Long taskId, Long subtaskId, SubtaskDto subtaskUdpateDto) {
        if(!taskListRepository.existsByProjectIdAndTaskId(projectId, taskId)) {
            throw new TaskNotExistException();
        }

        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(SubtaskNotExistException::new);
        subtask.setName(subtaskUdpateDto.getName());
        subtask.setDescription(subtaskUdpateDto.getDescription());
        subtask.setIsDone(subtaskUdpateDto.getIsDone());
        subtaskRepository.save(subtask);

    }
}
