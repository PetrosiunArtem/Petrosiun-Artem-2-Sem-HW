package org.example.app.service;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import java.net.URL;
import java.util.List;

import org.example.app.repository.FileRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileService {
    private final FileRepositoryImpl filesRepository;

    public FileService(FileRepositoryImpl filesRepository) {
        this.filesRepository = filesRepository;
    }

    public String downloadFile(URL currentUrl, String fileId, String userId) {
        log.info("Функция по скачиванию файла вызвана в сервисе");
        return filesRepository.downloadFile(currentUrl, fileId, userId);
    }

    public void uploadFile(File file) throws FileMemoryOverflowException {
        log.info("Функция по загрузке файла вызвана в сервисе");
        filesRepository.uploadFile(file);
    }

    public List<String> getAllFiles() {
        log.info("Функция по показу всех файлов вызвана в сервисе");
        return filesRepository.getAllFiles();
    }

    public File getFile(String fileId) throws FileNotFoundException {
        log.info("Функция по взятию файла вызвана в сервисе");
        return filesRepository.getFile(fileId);
    }

    public void putFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по замене файла на новый вызвана в сервисе");
        filesRepository.putFile(fileId, newFile);
    }

    public File deleteFile(String fileId) throws FileNotFoundException {
        log.info("Функция по удалению файла вызвана в репозитории");
        return filesRepository.deleteFile(fileId);
    }

    public void patchFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по изменению файла вызвана в репозитории");
        filesRepository.patchFile(fileId, newFile);
    }
}