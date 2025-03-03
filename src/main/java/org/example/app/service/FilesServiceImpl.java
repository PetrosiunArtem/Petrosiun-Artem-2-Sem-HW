package org.example.app.service;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.repository.FilesRepositoryImpl;
import org.springframework.cache.annotation.Cacheable;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FilesServiceImpl implements FilesService {
    private final FilesRepositoryImpl filesRepository;

    // Потокобезопасен
    private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    public FilesServiceImpl(FilesRepositoryImpl filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Override
    public String downloadFile(URL currentUrl, String fileId, String userId) {
        log.info("Функция по скачиванию файла вызвана в сервисе");
        return filesRepository.downloadFile(currentUrl, fileId, userId);
    }

    // ExactlyOnce
    @Override
    public void uploadFile(File file) throws FileMemoryOverflowException {
        log.info("Функция по загрузке файла вызвана в сервисе");
        if (!processedFiles.add(file.fileName())) {
            log.info("File {} already being uploaded", file.fileName());
            return;
        }
        filesRepository.uploadFile(file);
    }

    @Cacheable("files")
    @Override
    public List<String> getAllFiles() {
        log.info("Функция по показу всех файлов вызвана в сервисе");
        return filesRepository.getAllFiles();
    }

    @Cacheable(
            cacheNames = {"getFile"},
            key = "{#fileId}")
    @Override
    public File getFile(String fileId) throws FileNotFoundException {
        log.info("Функция по взятию файла вызвана в сервисе");
        return filesRepository.getFile(fileId);
    }

    @Cacheable(
            cacheNames = {"putFile"},
            key = "{#fileId}")
    @Override
    public void putFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по замене файла на новый вызвана в сервисе");
        filesRepository.putFile(fileId, newFile);
    }

    @Cacheable(
            cacheNames = {"deleteFile"},
            key = "{#fileId}")
    @Override
    public File deleteFile(String fileId) throws FileNotFoundException {
        log.info("Функция по удалению файла вызвана в репозитории");
        return filesRepository.deleteFile(fileId);
    }

    @Cacheable(
            cacheNames = {"patchFile"},
            key = "{#fileId}")
    @Override
    public void patchFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по изменению файла вызвана в репозитории");
        filesRepository.patchFile(fileId, newFile);
    }
}