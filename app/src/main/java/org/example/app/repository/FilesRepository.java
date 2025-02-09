package org.example.app.repository;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
public class FilesRepository {

    private static final Logger log = LoggerFactory.getLogger(FilesRepository.class);

    public String download(URL currentUrl, String bucketName, String fileName) {
        log.info("Функция по скачиванию файла вызвана в репозитории");
        return "Файл успешно скачан";
    }

    public String upload(String bucketName, InputStream file) {
        log.info("Функция по загрузке файла вызвана в репозитории");
        return "Файл успешно загружен";
    }
}