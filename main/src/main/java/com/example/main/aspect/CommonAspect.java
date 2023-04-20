package com.example.main.aspect;

import com.example.main.entity.AppUser;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j
public class CommonAspect {
//    @Pointcut("execution(public String sendToEmail(com.example.main.entity.AppUser))")
//    private void saveLogger(){}

    @Around("execution(public String sendToEmail(com.example.main.entity.AppUser))")
    public void createAppUser() {
        log.info("Hello");
    }
}