package org.example.app.repository;

import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import java.net.URL;
import java.util.List;

public interface FileRepository {

    String downloadFile(URL currentUrl, String bucketName, String fileName);

    void uploadFile(File file) throws FileMemoryOverflowException;

    File getFile(String fileId) throws FileNotFoundException;

    List<String> getAllFiles();

    void putFile(String fileId, File newFile) throws FileNotFoundException;

    File deleteFile(String fileId) throws FileNotFoundException;

    void patchFile(String fileId, File newFile) throws FileNotFoundException;

}
