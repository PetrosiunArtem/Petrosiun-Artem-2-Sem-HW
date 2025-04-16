package org.example.app.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.app.AppApplication;
import org.example.app.config.DatabaseConfig;
import org.example.app.controller.FilesControllerImpl;
import org.example.app.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {AppApplication.class, SecurityConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class LoggingAspectTest extends DatabaseConfig {

    @Autowired
    private FilesControllerImpl filesController;

    @Autowired
    private LoggingAspect loggingAspect;

    @Container
    @ServiceConnection
    public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @Test
    void shouldIncreaseClassFieldByTwo() throws MalformedURLException, JsonProcessingException {
        int count = loggingAspect.getExecutionCount();
        filesController.downloadFile(1L, 2L);
        assertEquals(count + 2, loggingAspect.getExecutionCount());
    }
}