package org.example.app.repository;

import lombok.RequiredArgsConstructor;
import org.example.app.config.DatabaseConfig;
import org.example.app.entity.Tag;
import org.example.app.exception.TagNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
class TagsRepositoryTest extends DatabaseConfig {

  @Autowired private TagsRepository tagsRepository;

  @Test
  void shouldFailToFindById() {
    Tag tag = new Tag("basketball");
    tagsRepository.save(tag);
    Optional<Tag> response = tagsRepository.findById(2000L);
    assertTrue(response.isEmpty());
  }

  @Test
  void shouldSuccessfullyFindById() throws TagNotFoundException {
    Tag tag = new Tag("football");
    tagsRepository.save(tag);
    Tag response = tagsRepository.findById(tag.getId()).orElseThrow(TagNotFoundException::new);
    assertEquals(1, response.getId());
    assertEquals(response.getName(), tag.getName());
  }
}
