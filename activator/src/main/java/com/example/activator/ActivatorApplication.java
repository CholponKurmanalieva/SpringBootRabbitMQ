package com.example.activator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.main", "com.example.activator"})
public class ActivatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivatorApplication.class, args);
    }

}
