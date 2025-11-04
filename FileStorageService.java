package com.example.ticketing.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final Path root = Paths.get(System.getProperty("user.dir"), "uploads");

    public FileStorageService() throws IOException {
        Files.createDirectories(root);
    }

    public String store(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path dest = root.resolve(System.currentTimeMillis() + "_" + filename);
        Files.copy(file.getInputStream(), dest);
        return dest.getFileName().toString();
    }

    public Path load(String filename){ return root.resolve(filename); }
}
