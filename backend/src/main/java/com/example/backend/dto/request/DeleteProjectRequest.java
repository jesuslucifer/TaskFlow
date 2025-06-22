package com.example.backend.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DeleteProjectRequest {
    Long id;
    //String name;
}
