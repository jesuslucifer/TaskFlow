package com.example.backend.dto.response;

import com.example.backend.model.Subtask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubtaskDto {
    private long id;
    private String name;
    private String description;
    private Boolean isDone;

    public SubtaskDto() {}

    public SubtaskDto(Subtask subtask) {
        this.id = subtask.getId();
        this.name = subtask.getName();
        this.description = subtask.getDescription();
        this.isDone = subtask.getIsDone();
    }
}
