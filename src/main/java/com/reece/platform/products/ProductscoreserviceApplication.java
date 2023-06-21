package com.reece.platform.products;

import java.util.concurrent.Executor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class ProductscoreserviceApplication {

    public static void main(String[] args) {
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        SpringApplication.run(ProductscoreserviceApplication.class, args);
    }
}
