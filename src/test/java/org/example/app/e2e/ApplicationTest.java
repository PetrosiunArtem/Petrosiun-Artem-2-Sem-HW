package org.example.app.e2e;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.app.entity.File;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void End2EndTest() {
        // Step 1: Create files
        File mockFile1 = new File("book.txt", 48 * 1024);
        File mockFile2 = new File("secrets.txt", 1024 * 1024);
        ResponseEntity<File> uploadResponse1 =
                restTemplate.postForEntity("http://localhost:" + port + "/second-memory/files/upload", mockFile1, File.class);

        ResponseEntity<File> uploadResponse2 =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/second-memory/files/upload", mockFile2, File.class);
        assertEquals(HttpStatus.CREATED, uploadResponse1.getStatusCode());
        assertEquals(mockFile1, uploadResponse1.getBody());
        assertEquals(HttpStatus.CREATED, uploadResponse2.getStatusCode());
        assertEquals(mockFile2, uploadResponse2.getBody());

        // Step 2: Update file
        File updatedMockUser = new File("minecraft.exe", mockFile1.capacity());
        File updateResponse =
                restTemplate.patchForObject(
                        "http://localhost:" + port + "/second-memory/files/patch/1", updatedMockUser, File.class);

        assertEquals(updatedMockUser, updateResponse);

        // Step 3: Get file
        ResponseEntity<File> getUserResponse =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/second-memory/files/get/1", File.class);

        assertEquals(HttpStatus.OK, getUserResponse.getStatusCode());
        assertEquals(updatedMockUser, getUserResponse.getBody());

        // Step 4: Get all files
        List<String> fileArrayResponse =
                restTemplate.getForObject("http://localhost:" + port + "/second-memory/files", List.class);

        assertEquals(2, fileArrayResponse.size());
        assertEquals("1", fileArrayResponse.get(1));

        // Step 5: Delete file
        ResponseEntity<File> deleteResponse =
                restTemplate.exchange(
                        "http://localhost:" + port + "/second-memory/files/delete/1",
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        File.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals(updatedMockUser, deleteResponse.getBody());
    }
}
