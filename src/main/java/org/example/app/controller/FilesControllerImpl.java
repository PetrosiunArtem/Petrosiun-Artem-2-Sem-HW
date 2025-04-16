package org.example.app.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.mapper.FileMapper;
import org.example.app.service.FilesServiceImpl;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@Slf4j
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "CircuitBreakerAPI")
@RequiredArgsConstructor
public class FilesControllerImpl implements FilesController {
    private final FilesServiceImpl filesService;
    private final FileMapper fileMapper;

    @Override
    @GetMapping("/files/info/download/{fileId}/{userId}")
    public ResponseEntity<String> downloadFile(@PathVariable Long fileId, @PathVariable Long userId) throws MalformedURLException, JsonProcessingException {
        URL currentURL = new URL("https://localhost:8080/second-memory/files/info/download/" + fileId + "/" + userId);
        return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(filesService.downloadFile(currentURL, fileId, userId));
    }

    @Override
    @PostMapping("/files/upload")
    public ResponseEntity<FileDto> postUploadPage(@RequestBody File file) throws FileMemoryOverflowException, JsonProcessingException {
        FileDto fileDto = filesService.uploadFile(file);
        log.info("File uploaded successfully");
        return ResponseEntity.status(201).header("fileId", String.valueOf(file.getId())).body(fileDto);
    }

    @Override
    @GetMapping("/files/get/{fileId}")
    public ResponseEntity<FileDto> getFile(@PathVariable Long fileId) throws FileNotFoundException, JsonProcessingException {
        FileDto fIleDto = filesService.getFile(fileId);
        return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(fIleDto);
    }

    @Override
    @GetMapping("/files")
    public ResponseEntity<List<Long>> getAllFiles() throws JsonProcessingException {
        return ResponseEntity.ok().body(filesService.getAllFiles());
    }

    @Override
    @DeleteMapping("/files/delete/{fileId}")
    public ResponseEntity<FileDto> deleteFile(@PathVariable Long fileId) throws FileNotFoundException, JsonProcessingException {
        FileDto fIleDto = filesService.deleteFile(fileId);
        return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(fIleDto);
    }

    @Override
    @PutMapping("/files/put/{fileId}")
    public ResponseEntity<FileDto> putFile(@PathVariable Long fileId, @RequestBody File newFile) throws FileNotFoundException, JsonProcessingException {
        filesService.putFile(fileId, newFile);
        return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(fileMapper.toDto(newFile));
    }

    @Override
    @PatchMapping("/files/patch/{fileId}")
    public ResponseEntity<FileDto> patchFile(@PathVariable Long fileId, @RequestBody File newFile) throws FileNotFoundException, JsonProcessingException {
        FileDto fileDto = filesService.patchFile(fileId, newFile);
        return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(fileDto);
    }
}