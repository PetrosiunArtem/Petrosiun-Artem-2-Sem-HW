package org.example.app.repository;

import org.example.app.config.DatabaseConfig;
import org.example.app.entity.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@DataJpaTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilesRepositoryTest extends DatabaseConfig {

    @Autowired
    private FilesRepository filesRepository;

    @Test
    void shouldSuccessfullyFindAllId() {
        File file_first = new File("test first file", 1024);
        File file_second = new File("test second file", 2 * 1024);
        filesRepository.save(file_first);
        filesRepository.save(file_second);
        List<Long> ids = filesRepository.findAllId();
        assertEquals(ids, List.of(file_first.getId(), file_second.getId()));
    }

    @Test
    void shouldFailToFindAllId() {
        File file_first = new File("test first file", 1024);
        File file_second = new File("test second file", 2 * 1024);
        filesRepository.save(file_first);
        filesRepository.save(file_second);
        List<Long> ids = filesRepository.findAllId();
        assertNotEquals(ids, List.of(file_second.getId(), file_first.getId()));
    }

    @Test
    void shouldFailToFindById() {
        File file = new File("test file", 1024);
        filesRepository.save(file);
        Optional<File> response = filesRepository.findById(2000L);
        assertEquals(response.isEmpty(), true);
    }

    @Test
    void shouldSuccessfullyFindById() {
        File file = new File("test file", 1024);
        filesRepository.save(file);
        Optional<File> response = filesRepository.findById(file.getId());
        assertEquals(response.get().getId(), file.getId());
        assertEquals(response.get().getName(), file.getName());
    }
}