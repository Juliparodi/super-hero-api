package com.superhero.annotations.impl;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class ExecutionTimeLoggerAspect {

    @Around("@annotation(com.superhero.annotations.ExecutionTimeLog)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        log.info("Request '{}' took {} ms to execute.", joinPoint.getSignature().getName(),
            endTime - startTime);

        return result;
    }

}
