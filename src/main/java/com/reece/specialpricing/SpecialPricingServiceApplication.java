package com.reece.specialpricing;

import net.lbruun.springboot.preliquibase.PreLiquibaseAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        PreLiquibaseAutoConfiguration.class
})
public class SpecialPricingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpecialPricingServiceApplication.class, args);
    }
}

