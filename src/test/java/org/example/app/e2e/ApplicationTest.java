package org.example.app.e2e;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.example.app.AppApplication;
import org.example.app.dto.FileDto;
import org.example.app.security.SecurityConfig;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ContextConfiguration(classes = {AppApplication.class, SecurityConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.flyway.enabled=false"})
@Slf4j
@Testcontainers
@ActiveProfiles("test")
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:17")
                    .withInitScript("db-init-script.sql")
                    .withDatabaseName("test database")
                    .withUsername("My user")
                    .withPassword("My password");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void End2EndTest() {
        log.info("Db host: {}", postgresContainer.getHost());
        log.info("Db port: {}", postgresContainer.getFirstMappedPort());
        log.info("PostgreSQL is running at: {}", postgresContainer.getJdbcUrl());

        // Step 1: Create files
        File mockFile1 = new File("book.txt", 48 * 1024);
        File mockFile2 = new File("secrets.txt", 1024 * 1024);
        ResponseEntity<FileDto> uploadResponse1 =
                restTemplate.postForEntity("http://localhost:" + port + "/second-memory/files/upload", mockFile1, FileDto.class);

        ResponseEntity<FileDto> uploadResponse2 =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/second-memory/files/upload", mockFile2, FileDto.class);
        assertEquals(HttpStatus.CREATED, uploadResponse1.getStatusCode());
        assertEquals(mockFile1.getName(), Objects.requireNonNull(uploadResponse1.getBody()).name());
        assertEquals(mockFile1.getCapacity(), uploadResponse1.getBody().capacity());
        assertEquals(HttpStatus.CREATED, uploadResponse2.getStatusCode());
        assertEquals(mockFile2.getName(), Objects.requireNonNull(uploadResponse2.getBody()).name());
        assertEquals(mockFile2.getCapacity(), uploadResponse2.getBody().capacity());

        // Step 2: Update file
        File updatedMockUser = new File("minecraft.exe", mockFile1.getCapacity());
        FileDto updateResponse =
                restTemplate.patchForObject(
                        "http://localhost:" + port + "/second-memory/files/patch/1", updatedMockUser, FileDto.class);

        //assertEquals(updatedMockUser, updateResponse);
        assertEquals(updatedMockUser.getName(), Objects.requireNonNull(updateResponse.name()));
        assertEquals(updatedMockUser.getCapacity(), updateResponse.capacity());

        // Step 3: Get file
        ResponseEntity<FileDto> getUserResponse =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/second-memory/files/get/1", FileDto.class);

        assertEquals(HttpStatus.OK, getUserResponse.getStatusCode());
//        assertEquals(updatedMockUser, getUserResponse.getBody());
        assertEquals(updatedMockUser.getName(), Objects.requireNonNull(getUserResponse.getBody()).name());
        assertEquals(updatedMockUser.getCapacity(), getUserResponse.getBody().capacity());
        // Step 4: Get all files
        List<Integer> fileArrayResponse =
                restTemplate.getForObject("http://localhost:" + port + "/second-memory/files", List.class);

        assertEquals(2, fileArrayResponse.size());
        assertEquals(1, fileArrayResponse.get(1));

        // Step 5: Delete file
        ResponseEntity<FileDto> deleteResponse =
                restTemplate.exchange(
                        "http://localhost:" + port + "/second-memory/files/delete/1",
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        FileDto.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
//        assertEquals(updatedMockUser, deleteResponse.getBody());
        assertEquals(updatedMockUser.getName(), Objects.requireNonNull(deleteResponse.getBody()).name());
        assertEquals(updatedMockUser.getCapacity(), deleteResponse.getBody().capacity());

    }
}
