package com.example.backend.dto.response;

import com.example.backend.model.TaskCategory;
import lombok.Data;

@Data
public class TaskCategoryDto {
    private String name;

    public TaskCategoryDto(TaskCategory category) {
        this.name = category.getName();
    }
}
