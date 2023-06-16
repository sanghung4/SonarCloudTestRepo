package com.reece.platform.picking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class PickingCoreServiceApplication {
    public static void main(String[] args) {
        System.out.println("Hello Picking App!");
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        SpringApplication.run(PickingCoreServiceApplication.class, args);
    }
}