package org.example.app.repository;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
@Slf4j
public class FilesRepositoryImpl implements FilesRepository {


    @Override
    public String download(URL currentUrl, String bucketName, String fileName) {
        log.info("Функция по скачиванию файла вызвана в репозитории");
        return "Файл успешно скачан";
    }


    @Override
    public String upload(String bucketName, InputStream file) {
        log.info("Функция по загрузке файла вызвана в репозитории");
        return "Файл успешно загружен";
    }
}