package com.reece.punchoutcustomerbff.configurations;

import com.reece.punchoutcustomerbff.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * A class that handles both allowing access of endpoints without auth,
 * and that also established a custom auth manager.
 *
 * @author john.valentino
 */
@Configuration
@Slf4j
public class WebSecurityConfig {

    @Profile("external-prod")
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    @EnableWebSecurity
    public static class ProdSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable();
            http
                .authorizeRequests(authorize ->
                    authorize.antMatchers(getAllowedUrls()).permitAll().anyRequest().authenticated()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        }

        // https://stackoverflow.com/questions/4664893/
        // //how-to-manually-set-an-authenticated-user-in-spring-security-springmvc
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Profile("!external-prod")
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    @EnableWebSecurity
    public static class NonProdSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private SecurityService securityService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable();

            http
                .authorizeRequests()
                .antMatchers(getAllowedUrls())
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        }

        @Bean
        public HttpSessionIdResolver httpSessionIdResolver() {
            return HeaderHttpSessionIdResolver.xAuthToken();
        }

        // https://stackoverflow.com/questions/4664893/
        // //how-to-manually-set-an-authenticated-user-in-spring-security-springmvc
        @Bean
        public AuthenticationManager customAuthenticationManager() {
            return new AuthenticationManager() {
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    return securityService.authenticate(authentication);
                }
            };
        }
    }

    public static String[] getAllowedUrls() {
        return new String[] {
            "/resources/**",
            "/",
            "/security/login",
            "/swagger-ui/**",
            "/v3/**",
            "/invalid",
            "/actuator/**",
        };
    }
}
