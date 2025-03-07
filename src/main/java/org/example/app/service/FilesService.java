package org.example.app.service;

import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import java.net.URL;
import java.util.List;

public interface FilesService {

    String downloadFile(URL currentUrl, String fileId, String userId);

    void uploadFile(File file) throws FileMemoryOverflowException;

    List<String> getAllFiles();

    File getFile(String fileId) throws FileNotFoundException;

    void putFile(String fileId, File newFile) throws FileNotFoundException;

    File deleteFile(String fileId) throws FileNotFoundException;

    void patchFile(String fileId, File newFile) throws FileNotFoundException;
}