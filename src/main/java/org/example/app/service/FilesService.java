package org.example.app.service;

import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import java.net.URL;
import java.util.List;

public interface FilesService {

    String downloadFile(URL currentUrl, Long fileId, Long userId);

    FileDto uploadFile(File file) throws FileMemoryOverflowException;

    List<Long> getAllFiles();

    FileDto getFile(Long fileId) throws FileNotFoundException;

    FileDto putFile(Long fileId, File newFile) throws FileNotFoundException;

    FileDto deleteFile(Long fileId) throws FileNotFoundException;

    FileDto patchFile(Long fileId, File newFile) throws FileNotFoundException;
}