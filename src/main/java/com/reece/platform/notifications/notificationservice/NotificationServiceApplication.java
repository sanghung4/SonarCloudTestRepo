package com.reece.platform.notifications.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.reece.platform.notifications"})
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class NotificationServiceApplication {

	public static void main(String[] args) {
		java.security.Security.setProperty("networkaddress.cache.ttl", "60");
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
