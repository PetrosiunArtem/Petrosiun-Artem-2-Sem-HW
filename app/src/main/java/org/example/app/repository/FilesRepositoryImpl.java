package org.example.app.repository;

import java.io.IOException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.HashMap;

@Repository
@Slf4j
public class FilesRepositoryImpl implements FilesRepository {
    private static final HashMap<String, File> files = new HashMap<>();
    private static final int CAPACITY = 10 * 1024 * 1024;

    @Override
    public String downloadFile(URL currentUrl, String bucketName, String fileName) {
        log.info("Функция по скачиванию файла вызвана в репозитории");
        return "Файл успешно скачан";
    }


    @Override
    public String uploadFile(String bucketName, InputStream file) throws FileMemoryOverflowException, IOException {
        log.info("Функция по загрузке файла вызвана в репозитории");
        if (file.available() > CAPACITY) {
            throw new FileMemoryOverflowException();
        }
        return "Файл успешно загружен";
    }

    @Override
    public void putFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по замене файла на новый вызвана в репозитории");
        if (!files.containsKey(fileId)) {
            throw new FileNotFoundException("No such file exists");
        }
        files.put(fileId, newFile);
    }

    @Override
    public File deleteFile(String fileId) throws FileNotFoundException {
        log.info("Функция по удалению файла вызвана в репозитории");
        if (!files.containsKey(fileId)) {
            throw new FileNotFoundException("No such file exists");
        }
        return files.remove(fileId);
    }

    @Override
    public void patchFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по изменению файла вызвана в репозитории");
        if (!files.containsKey(fileId)) {
            throw new FileNotFoundException("No such file exists");
        }
        files.put(fileId, newFile);
    }
}