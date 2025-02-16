package org.example.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.service.FilesService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class FilesControllerImpl implements FilesController {
    private final FilesService filesService;

    public FilesControllerImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @Override
    @GetMapping("/files/info/download/{fileId}/{userId}")
    public ResponseEntity<String> downloadFile(@PathVariable("fileId") String fileId, @PathVariable("userId") String userId) throws MalformedURLException {
        URL currentURL = new URL("https://localhost:4567/files/info/download/" + fileId + "/" + userId);
        return ResponseEntity.ok(filesService.downloadFile(currentURL, fileId, userId));
    }

    @Override
    @PostMapping("/files/upload/{bucketName}")
    public ResponseEntity<String> postUploadPage(@PathVariable("bucketName") String bucketName, MultipartFile multipartFile) throws IOException, FileMemoryOverflowException {
        InputStream file = multipartFile.getInputStream();
        return ResponseEntity.ok(filesService.uploadFile(bucketName, file));
    }

    @Override
    @GetMapping("/files/upload/{bucketName}")
    public ResponseEntity<String> getUploadPage(@PathVariable String bucketName) {
        return ResponseEntity.ok("Uploading in progress");
    }

    @Override
    @DeleteMapping("/files/delete/{fileId}")
    public ResponseEntity<File> deleteFile(@PathVariable("fileId") String fileId) throws FileNotFoundException {
        File file = filesService.deleteFile(fileId);
        return ResponseEntity.ok(file);
    }

    @Override
    @PutMapping("/files/put/{fileId}")
    public ResponseEntity<File> putFile(@PathVariable("fileId") String fileId, @RequestBody File newFile) throws FileNotFoundException {
        filesService.putFile(fileId, newFile);
        return ResponseEntity.ok(newFile);
    }

    @Override
    @PatchMapping("/files/patch/{fileId}")
    public ResponseEntity<File> patchFile(@PathVariable("fileId") String fileId, @RequestBody File newFile) throws FileNotFoundException {
        filesService.patchFile(fileId, newFile);
        return ResponseEntity.ok(newFile);
    }
}