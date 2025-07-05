package com.example.backend.dto.response;

import com.example.backend.model.Priority;
import com.example.backend.model.Status;
import com.example.backend.model.Subtask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubtaskDto {
    private String name;
    private String description;
    private Status status;
    private Priority priority;

    public SubtaskDto() {}

    public SubtaskDto(Subtask subtask) {
        this.name = subtask.getName();
        this.description = subtask.getDescription();
        this.status = subtask.getStatus();
        this.priority = subtask.getPriority();
    }
}
