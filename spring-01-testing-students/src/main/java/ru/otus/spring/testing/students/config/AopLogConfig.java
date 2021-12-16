package ru.otus.spring.testing.students.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@ConditionalOnProperty(
        name = "log",
        prefix = "enable.service",
        havingValue = "true"
)
public class AopLogConfig {

    @Around("within(ru.otus.spring.testing.students..*) && " +
            "@target(ru.otus.spring.testing.students.config.aop.ServiceWithAop)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        Object result = joinPoint.proceed();

        log.info("Method::{}; with args::{}; and result::{};", methodName, args, result);

        return result;
    }
}