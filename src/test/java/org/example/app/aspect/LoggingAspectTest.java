package org.example.app.aspect;

import org.example.app.controller.FilesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggingAspectTest {

    @Autowired
    private FilesController filesController;

    @Autowired
    private LoggingAspect loggingAspect;

    @Test
    void shouldIncreaseClassFieldByTwo() {
        int count = loggingAspect.getExecutionCount();
        filesController.getAllFiles();
        assertEquals(count + 2, loggingAspect.getExecutionCount());
    }
}