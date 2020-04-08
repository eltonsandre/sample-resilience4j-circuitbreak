package com.github.eltonsandre.sample.circuitbreak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@EnableFeignClients
@SpringBootApplication
public class CircuitbreakApplication {

    @Autowired
    public static ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(CircuitbreakApplication.class, args);
    }

}
