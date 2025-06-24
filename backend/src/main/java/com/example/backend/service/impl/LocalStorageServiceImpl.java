package com.example.backend.service.impl;

import com.example.backend.exception.UploadFileException;
import com.example.backend.service.LocalStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalStorageServiceImpl implements LocalStorageService {

    @Value("${backend.upload.dir}")
    private String uploadDir;

    @Value("${server.address:localhost}")
    private String serverAddress;

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            Path path = Paths.get(uploadDir);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "http://" + serverAddress + ":" + serverPort + "/uploads/avatars/" + fileName;
        } catch (IOException e) {
            throw new UploadFileException();
        }
    }
}
