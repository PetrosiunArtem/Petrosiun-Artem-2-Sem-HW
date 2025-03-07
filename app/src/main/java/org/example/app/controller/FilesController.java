package org.example.app.controller;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.example.app.service.FilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FilesController {
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("/files/info/download/{fileId}/{userId}")
    private ResponseEntity<String> download(@PathVariable("fileId") String fileId, @PathVariable("userId") String userId) throws MalformedURLException {
        URL currentURL = new URL("https://localhost:4567/files/info/download/" + fileId + "/" + userId);
        return ResponseEntity.ok(filesService.download(currentURL, fileId, userId));
    }

    @PostMapping("/files/upload/{bucketName}")
    private ResponseEntity<String> postUploadPage(@PathVariable("bucketName") String bucketName, MultipartFile multipartFile) throws IOException {
        InputStream file = multipartFile.getInputStream();
        return ResponseEntity.ok(filesService.upload(bucketName, file));
    }

    @GetMapping("/files/upload/{bucketName}")
    private ResponseEntity<String> getUploadPage(@PathVariable String bucketName) {
        return new ResponseEntity<>("Uploading in progress", HttpStatus.OK);
    }
}