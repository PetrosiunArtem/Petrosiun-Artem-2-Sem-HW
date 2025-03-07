package org.example.app.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution( * org.example.app.controller.*.*( .. ))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Перед вызовом метода: " +
                joinPoint.getSignature().getName());
    }

    @Around("execution( * org.example.app.controller.*.*( .. ))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Instant finishTime = Instant.now();
        System.out.println("Метод " + joinPoint.getSignature().getName()
                + " выполнен за " + (finishTime.toEpochMilli() - startTime.toEpochMilli()) + " мс");
        return result;
    }
}
