package org.example.app.repository;

import org.example.app.config.DatabaseConfig;
import org.example.app.entity.File;
import org.example.app.exception.FileNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilesRepositoryTest extends DatabaseConfig {

  @Autowired private FilesRepository filesRepository;

  @Test
  void shouldSuccessfullyFindAllId() {
    File firstFile = new File("test first file", 1024);
    File secondFile = new File("test second file", 2 * 1024);
    filesRepository.save(firstFile);
    filesRepository.save(secondFile);
    List<Long> ids = filesRepository.findAllId();
    assertEquals(ids, List.of(firstFile.getId(), secondFile.getId()));
  }

  @Test
  void shouldFailToFindAllId() {
    File firstFile = new File("test first file", 1024);
    File secondFile = new File("test second file", 2 * 1024);
    filesRepository.save(firstFile);
    filesRepository.save(secondFile);
    List<Long> ids = filesRepository.findAllId();
    assertNotEquals(ids, List.of(secondFile.getId(), firstFile.getId()));
  }

  @Test
  void shouldFailToFindById() {
    File file = new File("test file", 1024);
    filesRepository.save(file);
    Optional<File> response = filesRepository.findById(2000L);
    assertTrue(response.isEmpty());
  }

  @Test
  void shouldSuccessfullyFindById() throws FileNotFoundException {
    File file = new File("test file", 1024);
    filesRepository.save(file);
    File response = filesRepository.findById(file.getId()).orElseThrow(FileNotFoundException::new);
    assertEquals(response.getId(), file.getId());
    assertEquals(response.getName(), file.getName());
  }
}
