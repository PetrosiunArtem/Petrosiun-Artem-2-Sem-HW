package org.example.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.mapper.FileMapper;
import org.example.app.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {
    private final FilesRepository filesRepository;

    @Autowired
    private final FileMapper fileMapper;
    private static final int CAPACITY = 10 * 1024 * 1024;
    // Потокобезопасен
    private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    @Override
    @Transactional
    public String downloadFile(URL currentUrl, Long fileId, Long userId) {
        log.info("Функция по скачиванию файла вызвана в сервисе");
        return "Файл успешно загрузился";
    }

    @Override
    @Transactional
    public FileDto uploadFile(File file) throws FileMemoryOverflowException {
        log.info("Функция по загрузке файла вызвана в сервисе");
        if (!processedFiles.add(file.getName())) {
            log.info("File {} already being uploaded", file.getName());
            return fileMapper.toDto(file);
        }
        if (file.getCapacity() > CAPACITY) {
            throw new FileMemoryOverflowException();
        }
        filesRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Transactional
    @Cacheable("files")
    @Override
    public List<Long> getAllFiles() {
        log.info("Функция по показу всех файлов вызвана в сервисе");
        return filesRepository.findAllId();
    }

    @Transactional
    @Cacheable(
            cacheNames = {"getFile"},
            key = "{#fileId}")
    @Override
    public FileDto getFile(Long fileId) throws FileNotFoundException {
        log.info("Функция по взятию файла вызвана в сервисе");
        Optional<File> response = filesRepository.findById(fileId);
        if (response.isEmpty()) {
            throw new FileNotFoundException();
        }
        File file = response.get();
        return fileMapper.toDto(file);
    }

    @Transactional
    @Cacheable(
            cacheNames = {"putFile"},
            key = "{#fileId}")
    @Override
    public FileDto putFile(Long fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по замене файла на новый вызвана в сервисе");
        Optional<File> response = filesRepository.findById(fileId);
        if (response.isEmpty()) {
            throw new FileNotFoundException();
        }
        File file = response.get();
        file.setName(newFile.getName());
        filesRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Transactional
    @Cacheable(
            cacheNames = {"deleteFile"},
            key = "{#fileId}")
    @Override
    public FileDto deleteFile(Long fileId) throws FileNotFoundException {
        log.info("Функция по удалению файла вызвана в сервисе");
        Optional<File> response = filesRepository.findById(fileId);
        if (response.isEmpty()) {
            throw new FileNotFoundException();
        }
        File file = response.get();
        filesRepository.delete(file);
        return fileMapper.toDto(file);
    }

    @Transactional
    @Cacheable(
            cacheNames = {"patchFile"},
            key = "{#fileId}")
    @Override
    public FileDto patchFile(Long fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по изменению файла вызвана в сервисе");
        Optional<File> response = filesRepository.findById(fileId);
        if (response.isEmpty()) {
            throw new FileNotFoundException();
        }
        File file = response.get();
        file.setName(newFile.getName());
        filesRepository.save(file);
        return fileMapper.toDto(file);
    }
}