package com.example.ticketing.controller;

import com.example.ticketing.service.FileStorageService;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileStorageService storage;
    public FileController(FileStorageService storage){ this.storage = storage; }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = storage.store(file);
        return ResponseEntity.ok(java.util.Map.of("filename", filename));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Path p = storage.load(filename);
        Resource r = new PathResource(p);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + p.getFileName().toString() + "\"")
                .body(r);
    }
}
