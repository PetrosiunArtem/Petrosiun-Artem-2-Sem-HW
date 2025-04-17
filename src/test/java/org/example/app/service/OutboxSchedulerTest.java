package org.example.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.example.app.AppApplication;
import org.example.app.dto.Action;
import org.example.app.dto.MessageDto;
import org.example.app.entity.Outbox;
import org.example.app.repository.OutboxRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ContextConfiguration(classes = {AppApplication.class})
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = {"spring.flyway.enabled=false", "topic-to-send-message=my_test_topic"})
@Testcontainers
public class OutboxSchedulerTest {

  @Autowired private OutboxScheduler outboxScheduler;
  @Autowired private OutboxRepository outboxRepository;
  @Autowired private ObjectMapper objectMapper;

  @Container @ServiceConnection
  public static final org.testcontainers.containers.KafkaContainer KAFKA =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

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
  void shouldSendMessageToKafkaSuccessfully() throws JsonProcessingException {
    MessageDto testDtoMessage =
        new MessageDto(10L, Instant.now(), Action.SELECT, "select file with id: 10");
    outboxRepository.save(
        Outbox.builder().data(objectMapper.writeValueAsString(testDtoMessage)).build());
    assertDoesNotThrow(() -> outboxScheduler.processOutbox());
    KafkaTestConsumer consumer =
        new KafkaTestConsumer(KAFKA.getBootstrapServers(), "some-group-id");
    consumer.subscribe(List.of("my_test_topic"));
    ConsumerRecords<String, String> records = consumer.poll();
    assertEquals(1, records.count());
    records
        .iterator()
        .forEachRemaining(
            record -> {
              MessageDto message = null;
              try {
                message = objectMapper.readValue(record.value(), MessageDto.class);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
              assertEquals(testDtoMessage, message);
            });
  }

  @Test
  void shouldFailSavingOutboxIfDataIsNull() {
    assertThrows(
        DataIntegrityViolationException.class,
        () -> outboxRepository.save(Outbox.builder().data(null).build()));
  }
}
