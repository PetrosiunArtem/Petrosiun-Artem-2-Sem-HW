package org.example.app.repository;

import java.io.InputStream;
import java.net.URL;

public interface FilesRepository {

    String download(URL currentUrl, String bucketName, String fileName);

    String upload(String bucketName, InputStream file);
}
