package org.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import java.net.URL;

import org.example.app.repository.FilesRepository;
import org.springframework.stereotype.Service;

@Service
public class FilesService {
    private final FilesRepository filesRepository;
    private static final Logger log = LoggerFactory.getLogger(FilesService.class);

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public String download(URL currentUrl, String file_id, String userId) {
        log.info("Функция по скачиванию файла вызвана в сервисе");
        return filesRepository.download(currentUrl, file_id, userId);
    }

    public String upload(String bucketName, InputStream file) {
        log.info("Функция по загрузке файла вызвана в сервисе");
        return filesRepository.upload(bucketName, file);
    }
}