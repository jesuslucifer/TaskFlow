package com.example.backend.exception;

public class ProjectNotExist extends RuntimeException {
    public ProjectNotExist(String message) {
        super(message);
    }
}
