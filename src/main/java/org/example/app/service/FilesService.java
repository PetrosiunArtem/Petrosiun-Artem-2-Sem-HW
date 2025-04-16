package org.example.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import java.net.URL;
import java.util.List;

public interface FilesService {

    String downloadFile(URL currentUrl, Long fileId, Long userId) throws JsonProcessingException;

    FileDto uploadFile(File file) throws FileMemoryOverflowException, JsonProcessingException;

    List<Long> getAllFiles() throws JsonProcessingException;

    FileDto getFile(Long fileId) throws FileNotFoundException, JsonProcessingException;

    FileDto putFile(Long fileId, File newFile) throws FileNotFoundException, JsonProcessingException;

    FileDto deleteFile(Long fileId) throws FileNotFoundException, JsonProcessingException;

    FileDto patchFile(Long fileId, File newFile) throws FileNotFoundException, JsonProcessingException;
}