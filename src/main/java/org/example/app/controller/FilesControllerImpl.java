package org.example.app.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.service.FilesServiceImpl;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@Slf4j
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "CircuitBreakerAPI")

public class FilesControllerImpl implements FilesController {
    private final FilesServiceImpl filesService;

    public FilesControllerImpl(FilesServiceImpl filesService) {
        this.filesService = filesService;
    }

    @Override
    @GetMapping("/files/info/download/{fileId}/{userId}")
    public ResponseEntity<String> downloadFile(@PathVariable("fileId") String fileId, @PathVariable("userId") String userId) throws MalformedURLException {
        URL currentURL = new URL("https://localhost:8080/second-memory/files/info/download/" + fileId + "/" + userId);
        return ResponseEntity.ok(filesService.downloadFile(currentURL, fileId, userId));
    }

    @Override
    @PostMapping("/files/upload")
    public ResponseEntity<File> postUploadPage(File file) throws FileMemoryOverflowException {
        filesService.uploadFile(file);
        log.info("File uploaded successfully");
        return ResponseEntity.status(201).body(file);
    }

    @Override
    @GetMapping("/files/get/{fileId}")
    public ResponseEntity<File> getFile(@PathVariable("fileId") String fileId) throws FileNotFoundException {
        File file = filesService.getFile(fileId);
        return ResponseEntity.ok(file);
    }

    @Override
    @GetMapping("/files")
    public ResponseEntity<List<String>> getAllFiles() {
        return ResponseEntity.ok(filesService.getAllFiles());
    }

    @Override
    @DeleteMapping("/files/delete/{fileId}")
    public ResponseEntity<File> deleteFile(@PathVariable("fileId") String fileId) throws FileNotFoundException {
        File file = filesService.deleteFile(fileId);
        return ResponseEntity.ok(file);
    }

    @Override
    @PutMapping("/files/put/{fileId}")
    public ResponseEntity<File> putFile(@PathVariable("fileId") String fileId, File newFile) throws FileNotFoundException {
        filesService.putFile(fileId, newFile);
        return ResponseEntity.ok(newFile);
    }

    @Override
    @PatchMapping("/files/patch/{fileId}")
    public ResponseEntity<File> patchFile(@PathVariable("fileId") String fileId, File newFile) throws FileNotFoundException {
        filesService.patchFile(fileId, newFile);
        return ResponseEntity.ok(newFile);
    }
}