package org.example.app.aspect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Getter
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private int executionCount = 0;


    @Before("execution( * org.example.app.controller.*.*( .. ))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Перед вызовом метода: {}", joinPoint.getSignature().getName());
    }

    @Around("execution( * org.example.app.controller.*.*( .. ))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {
        executionCount++;
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Instant finishTime = Instant.now();
        log.info("Метод {} выполнен за {} мс", joinPoint.getSignature().getName(), finishTime.toEpochMilli() - startTime.toEpochMilli());
        executionCount++;
        return result;
    }
}
