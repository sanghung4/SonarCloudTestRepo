package com.reece.platform.eclipse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan(basePackages={"com.reece.platform.eclipse"})
public class EclipseServiceApplication {

	public static void main(String[] args) {
		java.security.Security.setProperty("networkaddress.cache.ttl", "60");
		SpringApplication.run(EclipseServiceApplication.class, args);
	}

}
