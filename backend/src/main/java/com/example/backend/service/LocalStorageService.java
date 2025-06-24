package com.example.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface LocalStorageService {
    String uploadFile(MultipartFile file, String fileName);
}
