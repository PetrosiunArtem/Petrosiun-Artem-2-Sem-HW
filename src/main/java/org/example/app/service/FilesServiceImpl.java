package org.example.app.service;

import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.mapper.FileMapper;
import org.example.app.repository.FilesRepository;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesServiceImpl {
  private final FilesRepository filesRepository;

  private static final int MAX_CAPACITY_FOR_FILE = 10 * 1024 * 1024;
  // Потокобезопасен
  private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

  @Transactional
  public FileDto downloadFile(Part file) {
    log.debug("Функция по скачиванию файла вызвана в сервисе");
    File fileEntity = new File(file.getSubmittedFileName(), (int) file.getSize());
    filesRepository.save(fileEntity);
    return FileMapper.toDto(fileEntity);
  }

  // ExactlyOnce
  @Transactional
  public FileDto uploadFile(File file) throws FileMemoryOverflowException {
    log.debug("Функция по загрузке файла вызвана в сервисе");
    if (!processedFiles.add(file.getName())) {
      log.debug("File {} already being uploaded", file.getName());
      return FileMapper.toDto(file);
    }
    if (file.getCapacity() > MAX_CAPACITY_FOR_FILE) {
      throw new FileMemoryOverflowException();
    }
    filesRepository.save(file);
    return FileMapper.toDto(file);
  }

  @Transactional
  @Cacheable("files")
  public List<Long> getAllFiles() {
    log.debug("Функция по показу всех файлов вызвана в сервисе");
    return filesRepository.findAllId();
  }

  @Transactional
  @Cacheable(
      cacheNames = {"getFile"},
      key = "{#fileId}")
  public FileDto getFile(Long fileId) throws FileNotFoundException {
    log.debug("Функция по взятию файла вызвана в сервисе");
    File file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    return FileMapper.toDto(file);
  }

  @Transactional
  @Cacheable(
      cacheNames = {"putFile"},
      key = "{#fileId}")
  public FileDto putFile(Long fileId, File newFile) throws FileNotFoundException {
    log.debug("Функция по замене файла на новый вызвана в сервисе");
    File file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    file.setName(newFile.getName());
    filesRepository.save(file);
    return FileMapper.toDto(file);
  }

  @Transactional
  @Cacheable(
      cacheNames = {"deleteFile"},
      key = "{#fileId}")
  public FileDto deleteFile(Long fileId) throws FileNotFoundException {
    log.debug("Функция по удалению файла вызвана в сервисе");
    File file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    filesRepository.delete(file);
    return FileMapper.toDto(file);
  }

  @Transactional
  @Cacheable(
      cacheNames = {"patchFile"},
      key = "{#fileId}")
  public FileDto patchFile(Long fileId, File newFile) throws FileNotFoundException {
    log.debug("Функция по изменению файла вызвана в сервисе");
    File file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    file.setName(newFile.getName());
    filesRepository.save(file);
    return FileMapper.toDto(file);
  }
}
