package org.example.app.repository;

import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface FilesRepository {

    String downloadFile(URL currentUrl, String bucketName, String fileName);

    String uploadFile(String bucketName, InputStream file) throws FileMemoryOverflowException, IOException;

    void putFile(String fileId, File newFile) throws FileNotFoundException;

    File deleteFile(String fileId) throws FileNotFoundException;

    void patchFile(String fileId, File newFile) throws FileNotFoundException;

}
