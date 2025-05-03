package org.example.app.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.DistributionSummary;
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
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@Slf4j
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "CircuitBreakerAPI")
@Timed(
    value = "request.duration",
    description = "HTTP requests duration",
    percentiles = {0.50, 0.75, 0.95, 0.99},
    histogram = true)
public class FilesControllerImpl implements FilesController {
  private final FilesServiceImpl filesService;
  private final FileMapper fileMapper;
  private final Counter filesRequest;
  private final MeterRegistry registry;
  private final DistributionSummary heatmapDistribution;
  private final DistributionSummary histogramDistribution;

  public FilesControllerImpl(
      FilesServiceImpl filesService, FileMapper fileMapper, MeterRegistry registry) {
    this.filesService = filesService;
    this.fileMapper = fileMapper;
    this.filesRequest = Counter.builder("files.requests").tags("type", "total").register(registry);
    this.registry = registry;
    this.heatmapDistribution =
        DistributionSummary.builder("files.heatmap")
            .baseUnit("milliseconds")
            .serviceLevelObjectives(10, 50, 100, 200, 300, 400, 500, 1000, 10000)
            .register(registry);
    this.histogramDistribution =
        DistributionSummary.builder("files.histogram")
            .publishPercentiles(0.5, 0.75, 0.95, 0.99)
            .register(registry);
  }

  private Counter getFilesRequestCounter(String type) {
    return Counter.builder("files.requests")
        .description("Number of files requests by type")
        .tags("type", type)
        .register(registry);
  }

  @Override
  @GetMapping("/files/info/download/{fileId}/{userId}")
  public ResponseEntity<String> downloadFile(@PathVariable Long fileId, @PathVariable Long userId)
      throws MalformedURLException, JsonProcessingException {
    getFilesRequestCounter("download").increment();
    histogramDistribution.record(System.currentTimeMillis());
    heatmapDistribution.record(System.currentTimeMillis());
    URL currentURL =
        new URL(
            "https://localhost:8080/second-memory/files/info/download/" + fileId + "/" + userId);
    return ResponseEntity.ok()
        .header("fileId", String.valueOf(fileId))
        .body(filesService.downloadFile(currentURL, fileId, userId));
  }

  @Override
  @PostMapping("/files/upload")
  public ResponseEntity<FileDto> postUploadPage(@RequestBody File file)
      throws FileMemoryOverflowException, JsonProcessingException {
    getFilesRequestCounter("upload").increment();
    histogramDistribution.record(System.currentTimeMillis());
    heatmapDistribution.record(System.currentTimeMillis());
    FileDto fileDto = filesService.uploadFile(file);
    log.info("File uploaded successfully");
    return ResponseEntity.status(201).header("fileId", String.valueOf(file.getId())).body(fileDto);
  }

  @Override
  @GetMapping("/files/get/{fileId}")
  public ResponseEntity<FileDto> getFile(@PathVariable Long fileId)
      throws FileNotFoundException, JsonProcessingException {
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
  public ResponseEntity<FileDto> deleteFile(@PathVariable Long fileId)
      throws FileNotFoundException, JsonProcessingException {
    FileDto fIleDto = filesService.deleteFile(fileId);
    return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(fIleDto);
  }

  @Override
  @PutMapping("/files/put/{fileId}")
  public ResponseEntity<FileDto> putFile(@PathVariable Long fileId, @RequestBody File newFile)
      throws FileNotFoundException, JsonProcessingException {
    getFilesRequestCounter("update").increment();
    histogramDistribution.record(System.currentTimeMillis());
    heatmapDistribution.record(System.currentTimeMillis());
    filesService.putFile(fileId, newFile);
    return ResponseEntity.ok()
        .header("fileId", String.valueOf(fileId))
        .body(fileMapper.toDto(newFile));
  }

  @Override
  @PatchMapping("/files/patch/{fileId}")
  public ResponseEntity<FileDto> patchFile(@PathVariable Long fileId, @RequestBody File newFile)
      throws FileNotFoundException, JsonProcessingException {
    getFilesRequestCounter("update").increment();
    histogramDistribution.record(System.currentTimeMillis());
    heatmapDistribution.record(System.currentTimeMillis());
    FileDto fileDto = filesService.patchFile(fileId, newFile);
    return ResponseEntity.ok().header("fileId", String.valueOf(fileId)).body(fileDto);
  }
}
