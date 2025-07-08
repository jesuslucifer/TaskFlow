package com.example.backend.service;

import com.example.backend.dto.response.SubtaskDto;
import com.example.backend.model.Subtask;

import java.util.List;

public interface SubtaskService {
    Subtask save(Subtask subtask);

    Subtask createSubtask(Subtask subtask, Long projectId, Long taskId);

    void deleteSubtaskById(Long projectId, Long taskId, Long subtaskId);

    List<SubtaskDto> getAll(Long projectId, Long taskId);

    void updateSubtask(Long projectId, Long taskId, Long subtaskId, SubtaskDto subtaskDto);

    Subtask getById(Long projectId, Long taskId, Long subtaskId);

}
