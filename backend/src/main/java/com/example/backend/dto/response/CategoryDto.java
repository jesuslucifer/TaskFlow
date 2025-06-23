package com.example.backend.dto.response;

import com.example.backend.model.Category;
import lombok.Data;

@Data
public class CategoryDto {
    private String name;

    public CategoryDto(Category category) {
        this.name = category.getName();
    }
}
