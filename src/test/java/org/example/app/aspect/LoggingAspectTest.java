package org.example.app.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.app.AppApplication;
import org.example.app.config.DatabaseConfig;
import org.example.app.controller.FilesController;
import org.example.app.controller.FilesControllerImpl;
import org.example.app.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

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

    @Test
    void shouldIncreaseClassFieldByTwo() throws MalformedURLException, JsonProcessingException {
        int count = loggingAspect.getExecutionCount();
        filesController.downloadFile(1L, 2L);
        assertEquals(count + 2, loggingAspect.getExecutionCount());
    }
}