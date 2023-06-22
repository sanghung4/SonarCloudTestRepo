package com.reece.punchoutcustomerbff.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Used for putting the X-Auth-Token on every payload
 * @author john.valentino
 */
@Configuration
public class SwaggerConfiguration {

  @Value("${spring.application.name}")
  private String appName;

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title(appName).version("1.0.0"))
        .components(new Components()
            .addSecuritySchemes("X-Auth-Token", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-Auth-Token")))
        .addSecurityItem(new SecurityRequirement().addList("X-Auth-Token"));
  }

}
