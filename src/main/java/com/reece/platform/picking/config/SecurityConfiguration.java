package com.reece.platform.picking.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${auth.okta.disabled:false}")
    private Boolean isOktaDisabled;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        csrfTokenRequestAttributeHandler.setCsrfRequestAttributeName(null);

        if (isOktaDisabled) {
            http.authorizeHttpRequests((auth) -> auth.requestMatchers("/**").permitAll()).csrf((csrf) -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler));
        } else {
            http.exceptionHandling((exceptionHandling) ->
                            exceptionHandling
                                    .accessDeniedHandler(customAccessDeniedHandler))
                    .csrf((csrf) -> csrf
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler))
                    .authorizeHttpRequests((auth) -> auth.requestMatchers("/actuator/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated())
                    .oauth2ResourceServer((oauth) -> oauth
                            .jwt((jwt) -> jwt.jwtAuthenticationConverter(JwtAuthenticationToken::new)));

            Okta.configureResourceServer401ResponseBody(http);
        }

        return http.build();
    }
}
