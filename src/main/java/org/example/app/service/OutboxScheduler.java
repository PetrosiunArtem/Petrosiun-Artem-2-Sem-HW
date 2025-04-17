package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.example.app.entity.Outbox;
import org.example.app.repository.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OutboxScheduler {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final NewTopic topic;
  private final OutboxRepository outboxRepository;

  @Transactional
  @Scheduled(fixedDelay = 10000)
  public void processOutbox() {
    List<Outbox> result = outboxRepository.findAll();
    for (Outbox outboxRecord : result) {
      CompletableFuture<SendResult<String, String>> sendResult =
          kafkaTemplate.send(topic.name(), outboxRecord.getData());
      // block on sendResult until finished
    }
    outboxRepository.deleteAll(result);
  }
}
