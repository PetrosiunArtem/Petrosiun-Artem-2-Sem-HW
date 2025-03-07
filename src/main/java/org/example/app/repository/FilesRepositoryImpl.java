package org.example.app.repository;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class FilesRepositoryImpl implements FilesRepository {
    private static final HashMap<String, File> files = new HashMap<>();
    private static final int CAPACITY = 10 * 1024 * 1024;
    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClient webClient = WebClient.create();

    @Override
    public String downloadFile(URL currentUrl, String bucketName, String fileName) {
        log.info("Функция по скачиванию файла вызвана в репозитории");
        return "Файл успешно скачан";
    }


    @Override
    public void uploadFile(File file) throws FileMemoryOverflowException {
        log.info("Функция по загрузке файла вызвана в репозитории");
        if (file.capacity() > CAPACITY) {
            throw new FileMemoryOverflowException();
        }
        files.put(Integer.toString(files.size()), file);
        String response = restTemplate.getForObject("https://dzen.ru/", String.class);
        log.info(response);
    }

    @Override
    public File getFile(String fileId) throws FileNotFoundException {
        log.info("Функция по взятию файла вызвана в репозитории");
        if (!files.containsKey(fileId)) {
            throw new FileNotFoundException("No such file exists");
        }
        return files.get(fileId);
    }

    @Override
    public List<String> getAllFiles() {
        return new ArrayList<>(files.keySet());
    }

    @Override
    public void putFile(String fileId, File newFile) throws FileNotFoundException {
        log.info("Функция по замене файла на новый вызвана в репозитории");
        if (!files.containsKey(fileId)) {
            throw new FileNotFoundException("No such file exists");
        }
        files.replace(fileId, newFile);
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
        files.replace(fileId, newFile);
        String response =
                webClient
                        .get()
                        .uri("https://dzen.ru/")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
        log.info(response);
    }
}