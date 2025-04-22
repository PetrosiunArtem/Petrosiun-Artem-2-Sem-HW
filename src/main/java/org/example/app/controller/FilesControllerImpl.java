package org.example.app.controller;

import java.util.List;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.mapper.FileMapper;
import org.example.app.service.FilesServiceImpl;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "CircuitBreakerAPI")
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class FilesControllerImpl implements FilesController {
  private final FilesServiceImpl filesService;

  @Override
  public ResponseEntity<FileDto> downloadFile(Part file) {
    return ResponseEntity.ok(filesService.downloadFile(file));
  }

  @Override
  public ResponseEntity<FileDto> postUploadPage(File file) throws FileMemoryOverflowException {
    FileDto fileDto = filesService.uploadFile(file);
    return ResponseEntity.status(201).body(fileDto);
  }

  @Override
  public ResponseEntity<FileDto> getFile(Long fileId) throws FileNotFoundException {
    FileDto fIleDto = filesService.getFile(fileId);
    return ResponseEntity.ok(fIleDto);
  }

  @Override
  public ResponseEntity<List<Long>> getAllFiles() {
    return ResponseEntity.ok(filesService.getAllFiles());
  }

  @Override
  public ResponseEntity<FileDto> deleteFile(Long fileId) throws FileNotFoundException {
    FileDto fIleDto = filesService.deleteFile(fileId);
    return ResponseEntity.ok(fIleDto);
  }

  @Override
  public ResponseEntity<FileDto> putFile(Long fileId, File newFile) throws FileNotFoundException {
    filesService.putFile(fileId, newFile);
    return ResponseEntity.ok(FileMapper.toDto(newFile));
  }

  @Override
  public ResponseEntity<FileDto> patchFile(Long fileId, File newFile) throws FileNotFoundException {
    FileDto fileDto = filesService.patchFile(fileId, newFile);
    return ResponseEntity.ok(fileDto);
  }
}
