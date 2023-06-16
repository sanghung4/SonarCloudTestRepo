package com.reece.platform.eclipse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WMSEclipseCoreServiceApplication {

    public static void main(String[] args) {
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        SpringApplication.run(WMSEclipseCoreServiceApplication.class, args);
    }

}
