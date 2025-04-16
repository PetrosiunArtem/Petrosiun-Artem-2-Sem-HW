package org.example.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.example.app.configuration.KafkaTopicConfig;
import org.example.app.configuration.ObjectMapperConfig;
import org.example.app.dto.Action;
import org.example.app.dto.MessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.KafkaContainer;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest(
        classes = {KafkaProducerService.class},
        properties = {"topic-to-send-message=my_test_topic"}
)
@Import({KafkaAutoConfiguration.class, ObjectMapperConfig.class, KafkaTopicConfig.class})
@Testcontainers
class KafkaProducerServiceTest {

    @Container
    @ServiceConnection
    public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSendMessageToKafkaSuccessfully() throws InterruptedException {
        MessageDto testDtoMessage = new MessageDto(1L, Instant.now(), Action.SELECT, "select file with id: 1");

        KafkaTestConsumer consumer = new KafkaTestConsumer(KAFKA.getBootstrapServers(), "some-group-id");
        consumer.subscribe(List.of("my_test_topic"));
        ConsumerRecords<String, String> records = consumer.poll();

        // Я не знаю как сделать так, чтобы тесты не влияли друг на друга, поэтому пока будет так.
        TimeUnit.SECONDS.sleep(6);

        assertDoesNotThrow(() -> kafkaProducerService.sendMessage(testDtoMessage));
        records = consumer.poll();
        assertEquals(1, records.count());
        records.iterator().forEachRemaining(
                record -> {
                    MessageDto message = null;
                    try {
                        message = objectMapper.readValue(record.value(), MessageDto.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    assertEquals(testDtoMessage, message);
                }
        );
    }

    @Test
    void shouldFailSendMessageToKafka() throws InterruptedException {
        MessageDto testDtoMessage = new MessageDto(1L, Instant.now(), Action.SELECT, "select file with id: 1");

        assertDoesNotThrow(() -> kafkaProducerService.sendMessage(testDtoMessage));

        KafkaTestConsumer consumer = new KafkaTestConsumer(KAFKA.getBootstrapServers(), "another-group-id");
        consumer.subscribe(List.of("no_my_test_topic"));

        // Я не знаю как сделать так, чтобы тесты не влияли друг на друга, поэтому пока будет так.
        TimeUnit.SECONDS.sleep(6);

        ConsumerRecords<String, String> records = consumer.poll();
        assertEquals(0, records.count());
    }
}

