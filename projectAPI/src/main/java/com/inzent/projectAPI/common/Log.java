package com.inzent.projectAPI.common;

import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Log {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Around("within(com.inzent.projectAPI.controller.*)")
    public Object saveLogging(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        logger.debug("[ CONTROLLER RESULT ] : {}", result);
        return result;
    }

}
