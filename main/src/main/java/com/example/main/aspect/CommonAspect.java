package com.example.main.aspect;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j
public class CommonAspect {
    @Pointcut("execution(public String sendToEmail(com.example.main.entity.AppUser))")
    private void saveLogger(){}

    //TODO
}