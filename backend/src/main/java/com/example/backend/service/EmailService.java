package com.example.backend.service;

public interface EmailService {
    void send(String to, String subject, String body);
}
