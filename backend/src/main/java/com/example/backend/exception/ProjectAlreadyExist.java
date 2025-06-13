package com.example.backend.exception;

public class ProjectAlreadyExist extends RuntimeException {
    public ProjectAlreadyExist(String message) {
        super(message);
    }
}
