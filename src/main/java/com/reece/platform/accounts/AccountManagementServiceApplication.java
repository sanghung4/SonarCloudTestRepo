package com.reece.platform.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.reece.platform.accounts" })
@EnableJpaRepositories("com.reece.platform.accounts.model")
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class AccountManagementServiceApplication {

    public static void main(String[] args) {
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        SpringApplication.run(AccountManagementServiceApplication.class, args);
    }
}
